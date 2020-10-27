<template>
  <v-layout
    row
    wrap>

    <v-navigation-drawer
      v-model="open"
      width="500"
      right
      temporary
      fixed>
      <v-layout
        column
        px-2>
        <nodes v-model="nodes"></nodes>
      </v-layout>
    </v-navigation-drawer>
    <v-layout
      row
      wrap
    >
      <v-flex
        v-for="node in activeNodes"
        v-bind:key="node.id"
        sm12
        pb-2
      >
        <charts :kpis="kpis" :node="node" :records="records" :kpi="kpi"/>
      </v-flex>
    </v-layout>
  </v-layout>
</template>

<script>
  import {mapMutations, mapState} from 'vuex'
  import Nodes from '@/components/Nodes'
  import Charts from '@/components/charts'

  export default {
    async mounted () {
      let getUrl = window.location;
      let baseUrl = getUrl.protocol + '//' + getUrl.host + '/' + getUrl.pathname.split('/')[1];
      if (baseUrl.indexOf('https') > -1) {
        baseUrl = baseUrl.replace('https', 'wss')
      } else if (baseUrl.indexOf('http') > -1) {
        baseUrl = baseUrl.replace('http', 'ws')
      }
      // lol
      baseUrl = baseUrl.replace(':3000', ':8080');
      console.log(baseUrl);
      let c = await this.$stompConnect(`${baseUrl}dash`, this.updateRecords);
      this.stompClient = c
    },
    components: {
      Nodes,
      Charts
    },
    data () {
      return {
        kpi: {},
        nodes: [],
        kpis: [],
        records: {},
        stompClient: undefined
      }
    },
    async asyncData ({app}) {
      const nodes = await app.$axios.$get('api/nodes');
      const kpis = await app.$axios.$get('api/kpi');
      return {
        nodes, kpis
      }
    },
    watch: {
      nodes (nv) {
        let dashCfg = {dashboardNodes: []};
        nv.forEach(n => {
          if (n.active) {
            n.live = true;
            n.lastX = 60;
            n.historyStart = '';
            n.historyEnd = '';
            if (n.mode === 'live' && n.lastX) {
              // live
              n['mode'] = 'live';
              dashCfg.dashboardNodes.push(n)
            } else if (n.mode === 'hist' && n.historyStart && n.historyEnd) {
              // hist
              n['mode'] = 'hist';
              dashCfg.dashboardNodes.push(n)
            }
          }
        });
        if (dashCfg.dashboardNodes.length > 0) {
          this.stompClient.configureDashboard(dashCfg)
        }
      }
    },
    computed: {
      activeNodes () {
        return this.nodes.filter(function (n) {
          return n.active
        })
      },
      ...mapState({
        isOpen: state => state.options.optionsOpen
      }),
      open: {
        get () {
          return this.isOpen
        },
        set (nv) {
          this.setOpen(nv)
        }
      }
    },
    methods: {
      ...mapMutations({
        setOpen: 'options/setOpen'
      }),
      updateRecords (records) {
        this.records = records
      }
    }
  }
</script>

<style scoped>
  .v-card__title > .v-input {
    color: #000;
    margin-top: 0;
    padding-top: 0;
  }
</style>

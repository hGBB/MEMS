<template>
  <v-layout
    column
    wrap>
    <v-flex>
      <v-card>
        <inputForm
          v-model="newAlertRule"
          v-bind:kpis="kpi"
          v-bind:nodes="node"
          v-bind:create="true"
        >
        </inputForm>
      </v-card>
    </v-flex>
    <v-flex>
      <v-btn @click="createAr"
             color="success">Save
      </v-btn>
    </v-flex>
  </v-layout>
</template>
<script>
  import inputForm from '@/components/inputForm'

  export default {
    data() {
      return {
        newAlertRule: {}
      }
    },
    async asyncData({app, params}) {
      const kpi = await app.$axios.$get(`api/kpi`);
      const node = await app.$axios.$get("/api/nodes");
      return {kpi, node}
    },
    components: {
      inputForm
    },
    methods: {
      createAr() {
        this.$axios.$post("api/alerts", this.newAlertRule).then(res => {
          alert('saved!');
          this.$router.push("/alert")
        })
      }
    }
  }
</script>

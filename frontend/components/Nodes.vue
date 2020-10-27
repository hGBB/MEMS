<template>
  <v-layout column>
    <v-flex xs12>
      <v-text-field placeholder="Search Nodes" prepend-icon="search" v-model="search"></v-text-field>
    </v-flex>
    <v-subheader>Nodes</v-subheader>
    <v-flex xs12>
      <v-card
        :key="n.id"
        v-for="n in displayNodes">
        <v-card-title>
          <v-switch
            :label="n.name"
            v-model="n['active']"/>
          <v-badge color="green" overlap>
            <span class="caption" slot="badge" style="font-size: 10px !important;">{{n.sendInterval}}</span>
            <v-icon color="grey darken-1" large> refresh</v-icon>
          </v-badge>
        </v-card-title>
        <v-card-actions
          v-if="n.active"
          column>
          <v-layout
            column>
            <v-flex xs12>
              <v-radio-group
                v-model="n['mode']"
                row>
                <v-radio
                  :value="'live'"
                  v-on:change="logConfig()"
                  label="Live"/>
                <v-radio
                  :value="'hist'"
                  v-on:change="logConfig()"
                  label="History"/>
              </v-radio-group>
            </v-flex>
            <v-flex xs12>
              <v-layout
                v-if="n.mode && n.mode === 'hist'"
                row>
                <v-flex xs6>
                  <v-menu
                    lazy
                    full-width
                    min-width="290px">
                    <v-text-field
                      slot="activator"
                      v-model="n['historyStart']"
                      id="start"
                      label="Start"
                      v-on:change="logConfig()"
                      prepend-icon="event"/>
                  </v-menu>
                </v-flex>
                <v-flex xs6>
                  <v-menu
                    lazy
                    full-width
                    min-width="290px">
                    <v-text-field
                      slot="activator"
                      v-model="n['historyEnd']"
                      id="end"
                      label="End"
                      v-on:change="logConfig()"
                      prepend-icon="event"/>
                  </v-menu>
                </v-flex>
              </v-layout>
              <v-layout
                v-if="n.mode && n.mode === 'live'"
                row>
                <v-flex
                  xs12
                  mx-2>
                  <v-text-field
                    v-model="n['lastX']"
                    id="last"
                    v-on:change="logConfig()"
                    label="Show last x min."/>
                </v-flex>
                <!--<v-flex
                  xs6
                  mx-2>
                  <v-text-field
                    id="resolution"
                    v-on:change="logConfig()"
                    v-model="n['resolution']"
                    label="Resolution %"/>
                </v-flex>-->
              </v-layout>
            </v-flex>
          </v-layout>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>


<script>


  export default {

    async mounted() {
    },
    name: 'NodesIterator',
    props: {
      value: Array
    },
    watch: {
      value: {
        deep: true,
        handler(nv) {
          this.$emit('input', nv)
        }
      }
    },
    data() {
      return {

        search: ''
      }
    },
    computed: {
      displayNodes() {
        if (this.search) {
          return this.value.filter(n => n.name.includes(this.search))
        }
        return this.value
      }
    },

    methods: {

      logConfig() {
        var config = {
          history: true,
          historyStart: '2018-10-13 12:00:00',
          historyEnd: '2018-10-13 14:00:00',
          live: false,
          lastX: 0,
          resolution: 0
        }
      }
    }
  }
</script>

<style scoped>

</style>

<template>
  <v-card>
    <v-layout
      row
      wrap
    >
      <v-flex>
        <v-card-text
          style="font-size: x-large"
        >{{node.name}}
        </v-card-text>
      </v-flex>
      <v-flex>
        <v-tabs
          right
          v-model="model"
        >
          <v-tab
            :key="kpi.id"
            v-for="kpi in kpis"
          >{{kpi.name}}
          </v-tab>
        </v-tabs>
      </v-flex>
    </v-layout>
    <v-tabs-items v-model="model">
      <v-tab-item
        v-for="kpi in kpis"
        :key="kpi.id"
      >
        <v-card>{{node.mode}}</v-card>
        <v-card>
          <v-flex>
            <history :kpi="kpi" :node="node" :records="records" :recordsOnNode="parseJson"
                     style="height: 40vh"></history>
          </v-flex>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </v-card>
</template>

<script>
  import History from './historyChart'

  export default {
    components: {
      History
    },
    props: {
      node: Object,
      kpis: Array,
      records: {},

    },
    data() {
      return {
        model: 'tab-1',
        recordsOnNode: []
      }
    },
    computed: {
      parseJson: {

        get() {
          if (typeof this.records !== "string") {
            return {}
          } else {
          var json = JSON.parse(this.records)
          var oneNode = []
          for (var i = 0; i < json.nodeRecords.length; i++) {
            if (json.nodeRecords[i].nodeId === this.node.id) {
              for (var j = 0; j < json.nodeRecords[i].records.length; j++) {
                  oneNode.push(json.nodeRecords[i].records[j])

              }
            }
           }
          }
          return oneNode
        }
      }
    }
  }
</script>

<style scoped>

</style>

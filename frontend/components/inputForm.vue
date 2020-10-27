<template>
  <v-layout>
    <v-flex>
      <v-card>
        <v-window v-model="step">
          <v-window-item :value="1">
            <v-card-title class="headline">
              Alert Rule: {{value.name}}
            </v-card-title>
            <v-flex
              offset-sm1
              sm10>
              <v-text-field
                label="Alert Rule Name:"
                v-model="value.name"
              >
              </v-text-field>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <v-select
                :items="methods"
                item-text="name"
                label="Choose method"
                return-object
                v-model="value.method"
              ></v-select>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <v-text-field
                label="Timespan:"
                suffix="in minutes"
                v-model="value.timespan"
              >
              </v-text-field>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <v-text-field
                label="Sleep Period:"
                suffix="in minutes"
                v-model="value.sleep"
              >
              </v-text-field>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <p>

                <v-btn :disabled="step===1"
                       @click="step--"
                       color="primary">
                  <v-icon>keyboard_arrow_left</v-icon>
                  Previous
                </v-btn>

                <v-btn :disabled="create === true"
                       @click="step++"
                       color="primary">
                  <v-icon>keyboard_arrow_right</v-icon>
                  Next
                </v-btn>
              </p>
            </v-flex>
          </v-window-item>
          <v-window-item :value="2">
            <v-card-title class="headline">
              Key Performance Indicators:
            </v-card-title>
            <v-flex
              offset-sm1
              sm10>
              <v-select
                :items="kpis"
                item-text="name"
                label="Add KPIs"
                return-object
                v-model="gKpis"
              ></v-select>
            </v-flex>


            <v-flex
              offset-sm1
              sm10>
              <v-data-table
                :headers="KpiHeader"
                :items="value.kpis"
                hide-actions>
                <template
                  slot="items"
                  slot-scope="props">
                  <td>{{props.item.kpiDTO.id}}</td>
                  <td>{{props.item.kpiDTO.name}}</td>
                  <td>{{props.item.kpiDTO.unit}}</td>
                  <td>

                    <v-edit-dialog
                      :return-value.sync="props.item.threshold"
                      large
                      lazy
                      persistent>
                      {{ props.item.threshold}}
                      <v-text-field
                        label="Edit"
                        single-line
                        slot="input"
                        v-model="props.item.threshold"/>
                    </v-edit-dialog>
                  </td>
                  <td>
                    <v-checkbox v-model="props.item.leq" value></v-checkbox>
                  </td>
                  <td>
                    <v-icon
                      @click="removeKpi(props.item)"
                      small
                    >
                      delete
                    </v-icon>
                  </td>
                </template>
              </v-data-table>
            </v-flex>


            <v-flex
              offset-sm1
              sm10>
              <p>
                <v-btn @click="step--"
                       color="primary">
                  <v-icon>keyboard_arrow_left</v-icon>
                  Previous
                </v-btn>
                <v-btn @click="step++"
                       color="primary">
                  <v-icon>keyboard_arrow_right</v-icon>
                  Next
                </v-btn>
              </p>
            </v-flex>
          </v-window-item>

          <v-window-item :value="3">
            <v-card-title class="headline">
              Nodes:
            </v-card-title>
            <v-flex
              offset-sm1
              sm10>
              <v-select
                :items="nodes"
                item-text="name"
                label="Assign Alert Rules to available Node"
                return-object
                v-model="allocateNode"
              ></v-select>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <v-data-table
                :headers="nodeHeader"
                :items="value.nodes"
                hide-actions>
                <template
                  slot="items"
                  slot-scope="props">
                  <td>{{props.item.id}}</td>
                  <td>{{props.item.name}}</td>
                  <td>
                    <v-icon
                      @click="removeNode(props.item)"
                      small
                    >
                      delete
                    </v-icon>
                  </td>
                </template>
              </v-data-table>
            </v-flex>
            <v-flex
              offset-sm1
              sm10>
              <v-btn
                @click="step--"
                color="primary">
                <v-icon>keyboard_arrow_left</v-icon>
                Previous
              </v-btn>
              <v-btn :disabled="step===3"
                     @click="step++"
                     color="primary">
                Next
                <v-icon>keyboard_arrow_right</v-icon>
              </v-btn>
            </v-flex>
          </v-window-item>
        </v-window>
      </v-card>
    </v-flex>
  </v-layout>
</template>
<script>
  export default {
    data() {
      return {
        methods: ['AVERAGE', 'STANDARDDEVIATION', 'KURTOSIS'],
        KpiHeader: [
          {text: 'ID', value: 'id'},
          {text: 'Name', value: 'name'},
          {text: 'Unit', value: 'unit'},
          {text: 'Threshold', value: 'threshold'},
          {text: 'leq', value: 'leq'},
          {text: 'Actions', value: 'actions'}
        ],
        nodeHeader: [
          {text: 'ID', value: 'id'},
          {text: 'Name', value: 'name'},
          {text: 'Actions', value: 'actions'}
        ],
        step: 1,
        switch: true,
      }
    },
    props: {
      'value': Object,
      'kpis': {},
      'nodes': {},
      'create': '',

    },
    watch: {
      value: {
        handler(nv, ov) {
          this.$emit('input', nv)
        },
        deep: true
      }
    },
    methods: {
      removeNode(node) {
        let nodes = this.value.nodes;
        this.value.nodes = nodes.filter(elem => elem.id !== node.id)
      },
      removeKpi(kpi) {
        let kpis = this.value.kpis;
        this.value.kpis = kpis.filter(elem => elem !== kpi)
      }
    },
    computed: {
      gKpis: {
        get() {
          if (!this.value.kpis) return [];
          return this.value.kpis.map(k => k)
        },
        set(kpi) {
          if (!this.value.kpis) this.value.kpis = [];
          var alertKpi = {threshold: 0, leq: 0, kpiDTO: kpi};
          this.value.kpis.push(alertKpi)
        }
      },
      allocateNode: {
        get() {
          if (!this.value.nodes) return [];
          return this.value.nodes.map(n => n)
        },
        set(node) {
          if (!this.value.nodes) this.value.nodes = [];
          this.value.nodes.push(node)
        }
      }
    }
  }
</script>
<style>

</style>

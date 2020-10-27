<template>
  <v-layout
    row
    wrap
    my-2>
    <v-flex sm12>
      <v-card>
        <v-card-title class="headline">
          Node Settings
        </v-card-title>
        <v-card-text>
          <v-data-table
            :headers="headers"
            :items="nodes"
            hide-actions
            class="elevation-1">
            <template
              slot="items"
              slot-scope="props">
              <td>{{ props.item.id }}</td>
              <td>
                <v-edit-dialog
                  :return-value.sync="props.item.name"
                  @save="save(props.index)"
                  large
                  lazy
                  persistent>
                  {{ props.item.name}}
                  <v-text-field
                    label="Edit"
                    single-line
                    slot="input"
                    v-model="props.item.name"/>
                </v-edit-dialog>
              </td>
              <td>
                <v-edit-dialog
                  :return-value.sync="props.item.sendInterval"
                  lazy
                  @save="save(props.index)"
                  large
                  persistent>
                  {{ props.item.sendInterval }}
                  <v-text-field
                    slot="input"
                    v-model="props.item.sendInterval"
                    type="number"
                    label="Edit"
                    single-line/>
                </v-edit-dialog>
              </td>
              <td>
                <v-icon
                  @click="dialogWindow(props.item.id)"
                  small
                >
                  delete
                </v-icon>
              </td>
            </template>
          </v-data-table>
          <v-btn @click="$router.push('nodes/register')" absolute bottom color="green" dark fab large pa-2 right>
            <v-icon dark>add</v-icon>
          </v-btn>
        </v-card-text>
      </v-card>
    </v-flex>
    <v-dialog
      v-model="dialog"
      max-width="290"
    >
      <v-card>
        <v-card-title class="headline">Warning</v-card-title>

        <v-card-text>Are you sure you want do delete this node?</v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1"
            flat="flat"
            @click="dialog = false"
          >
            NO
          </v-btn>

          <v-btn
            color="red"
            flat="flat"
            @click="deleteItem(selectedNode)"
          >
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
  import {LEVEL} from '@/store/message'

  export default {
    async asyncData ({app, params}) {
      const nodes = await app.$axios.$get('api/nodes');
      return {nodes}
    },
    data () {
      return {
        selectedNode: null,
        dialog: false,
        headers: [
          {text: 'ID', value: 'id'},
          {text: 'Node', value: 'name'},
          {text: 'Monitoring Interval', value: 'sendInterval', sortable: false},
          {text: 'Actions', sortable: false}
        ],
      }
    },
    methods: {
      async deleteItem (id) {
        this.$axios.$delete(`api/nodes/${id}`).then(res => {
          this.$axios.$get(`api/nodes`).then(res => {
            this.nodes = res
          })
        })
        this.dialog= false
      },
      async save (i) {
        await this.$axios.$put(`api/nodes/${this.nodes[i].id}`, this.nodes[i]).then(res => {
          alert('saved!')
        })
      },
      dialogWindow (id) {
        this.selectedNode= id
        this.dialog= true
      }
    }
  }
</script>

<style scoped>

</style>

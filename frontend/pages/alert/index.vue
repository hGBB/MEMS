<template>
  <v-layout
    row
    my-2
    wrap>
    <v-flex>
      <v-card>
      <v-card-title class="headline">
        Alert Rules Overview
      </v-card-title>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="rules"
          hide-actions
          class="elevation-1">
          <template
            slot="items"
            slot-scope="props">
            <td>{{props.item.id }}</td>
            <td>{{props.item.name }}</td>
            <td>{{props.item.method}}</td>
            <td>{{props.item.sleep}}</td>
            <td>
              <v-icon
                @click="$router.push(`alert/${props.item.id}`)"
                class="mr-2"
                small
              >
                edit
              </v-icon>
              <v-icon
                @click="dialogWindow(props.item.id)"
                small
              >
                delete
              </v-icon>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <v-container absolute bottom right>
    <v-btn @click="$router.push('alert/history')" id="button" color="indigo" dark fab large pa-2 >
      <v-icon dark>book</v-icon>
    </v-btn>
    <v-btn @click="$router.push('alert/create')" color="green" dark  fab large pa-2 >
      <v-icon dark>add</v-icon>
    </v-btn>
    </v-container>
    </v-flex>
    <v-dialog
      v-model="dialog"
      max-width="290"
    >
      <v-card>
        <v-card-title class="headline">Warning</v-card-title>

        <v-card-text>Are you sure you want do delete this alert?</v-card-text>

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
            @click="deleteAR(selectedAlert)"
          >
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>
<script>
  import Rule from '@/components/rule'

  export default {
    components: {
      Rule
    },
    data() {
      return {
        selectedAlert: null,
        dialog: false,
        headers: [
          {text: 'ID', value: 'id'},
          {text: 'AlertRule Name', value: 'name'},
          {text: 'Method', value: 'method', sortable: false},
          {text: 'Sleep Period', value: 'sleep', sortable: false},
          {text: 'Actions', sortable: false},
        ],
      }
    },
    async asyncData({app}) {
      const rules = await app.$axios.$get('api/alerts');
      return {rules}
    },
    methods: {
      async deleteAR(id) {
        this.$axios.$delete(`api/alerts/${id}`).then(res => {
          this.$axios.$get(`api/alerts`).then(res => {
            this.rules = res
          })
        })
        this.dialog= false
      },

      dialogWindow (id) {
        this.selectedAlert= id
        this.dialog= true
      }
    }
  }

</script>
<style scoped>

</style>

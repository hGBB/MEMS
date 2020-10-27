<template>
  <v-layout
    row
    wrap>
    <v-flex xs12>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0">Groups</h3>
          </div>
        </v-card-title>
        <v-card-text>
          <v-text-field
            flat
            label="Search"
            prepend-inner-icon="search"
            v-model="search"
          >asd
          </v-text-field>
          <v-data-table
            :headers="groupTableHeaders"
            :items="filteredGroups"
            class="elevation-1"
            hide-actions>
            <template
              slot="items"
              slot-scope="props">
              <td>{{ props.item.name }}</td>
              <td>
                <v-chip :key="u.id" v-for="u in props.item.users">{{u.login}}</v-chip>
              </td>
              <td>
                <v-chip :key="n.id" v-for="n in props.item.nodes">{{n.name}}</v-chip>
              </td>
              <td>
                <v-icon
                  @click="$router.push(`groups/edit/${props.item.id}`)"
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
    </v-flex>
    <v-btn @click="$router.push('groups/create')" absolute bottom color="green" dark fab large pa-2 right>
      <v-icon dark>add</v-icon>
    </v-btn>
    <v-dialog
      v-model="dialog"
      max-width="290"
    >
      <v-card>
        <v-card-title class="headline">Warning</v-card-title>

        <v-card-text>Are you sure you want do delete this group?</v-card-text>

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
            @click="deleteItem(selectedGroup)"
          >
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>

</template>
<script>

  export default {
    components: {},
    async asyncData ({app}) {
      const groups = await app.$axios.$get('api/groups');
      return {groups}
    },
    data () {
      return {
        dialog: false,
        selectedGroup: null,
        search: '',
        groupTableHeaders: [
          {text: 'Name', value: 'name'},
          {text: 'Users', sortable: false},
          {text: 'Nodes', sortable: false},
          {text: 'Actions', sortable: false}
        ]
      }
    }
    ,
    computed: {
      filteredGroups () {
        if (this.search && this.search.length > 0) {
          return this.groups.filter(g => g.name.toLowerCase().match(this.search.toLowerCase()))
        }
        return this.groups
      }
    }
    ,
    methods: {
      async deleteItem (id) {
        this.$axios.$delete(`api/groups/${id}`).then(res => {
          this.$axios.$get(`api/groups`).then(res => {
            this.groups = res
          })
        })
        this.dialog= false
      },
      dialogWindow (id) {
        this.selectedGroup= id
        this.dialog= true
      }
    }
  }

</script>
<style scoped>
  .rules {
    display: flex;
    flex-flow: wrap;
  }
</style>

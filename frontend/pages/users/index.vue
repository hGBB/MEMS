<template>
  <v-layout
    row
    wrap>
    <v-flex
      pa-2
      sm12>
      <v-card>
        <v-card-title
          class="headline">
          Overview Users
        </v-card-title>
        <v-card-text>
          <v-text-field
            flat
            label="Search"
            prepend-inner-icon="search"
            :append-icon="'clear'"
            @click:append="search = ''"
            v-model="search"
          >asd
          </v-text-field>
          <v-data-table
            :headers="userTableHeaders"
            :items="filteredUsers"
            class="elevation-1"
            hide-actions>
            <template
              slot="items"
              slot-scope="props">
              <td>{{ props.item.login }}</td>
              <td>{{ props.item.name }}</td>
              <td>
                <v-icon v-if="props.item.admin">done</v-icon>
              </td>
              <td>
                <v-icon v-if="props.item.privileged">done</v-icon>
              </td>
              <td>
                <v-icon
                  @click="$router.push(`users/edit/${props.item.id}`)"
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
      <v-btn @click="$router.push('users/create')" absolute bottom color="green" dark fab large pa-2 right>
        <v-icon dark>add</v-icon>
      </v-btn>
    </v-flex>
    <v-dialog
      v-model="dialog"
      max-width="290"
    >
      <v-card>
        <v-card-title class="headline">Warning</v-card-title>

        <v-card-text>Are you sure you want do delete this user?</v-card-text>

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
            @click="deleteItem(selectedUser)"
          >
            YES
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
  import User from '@/components/user'

  export default {
    components: {
      'userDetails': User
    },
    async asyncData ({app}) {
      const users = await app.$axios.$get('api/users')
      return {users}
    },
    data () {
      return {
        selectedUser: null,
        dialog: false,
        search: '',
        currentUser: {},
        userTableHeaders: [
          {text: 'Username', value: 'login'},
          {text: 'Full Name', value: 'name'},
          {text: 'Admin', value: 'admin'},
          {text: 'Privileged', value: 'privileged'},
          {text: 'Actions', sortable: false},
        ]
      }
    },
    computed: {
      filteredUsers () {
        return this.users.filter((user) => {
          return user.name.toLowerCase().match(this.search.toLowerCase())
        })
      },
    },
    methods: {
      setUserInForm (i) {
        this.currentUser = this.filteredUsers[i]
      },
      deleteItem (id) {
        this.$axios.$delete(`api/users/${id}`).then(res => {
          this.$axios.$get(`api/users`).then(res => {
            this.users = res
          })
        })
        this.dialog= false
      },
      dialogWindow (id) {
        this.selectedUser= id
        this.dialog= true
      }
    }
  }

</script>

<style scoped>

</style>

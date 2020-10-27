<template>
  <v-layout
    row
    wrap>
    <v-flex>
      <v-card>
        <v-card-title>
          Alert Notifications History
        </v-card-title>
        <v-data-table
          :headers="headers"
          :items="notifications"
          hide-actions>
          <template
            slot="items"
            slot-scope="props">
            <td>{{props.item.timestamp}}</td>
            <td>{{props.item.alertRuleId}}</td>
            <td>{{props.item.alertRuleName}}</td>
            <td>
              <v-icon
                @click="$router.push(`history/${props.item.id}`)"
                class="mr-2"
                small
              >
                edit
              </v-icon>
              <v-icon
                @click="deleteNotification(props.item.id)"
                class="mr-2"
                small
              >
                delete
              </v-icon>
            </td>
          </template>
        </v-data-table>
      </v-card>
    </v-flex>
  </v-layout>
</template>
<script>
  export default {

    data() {
      return {
        headers: [
          {text: 'Timestamp', value: 'timestamp'},
          {text: 'Alert Rule ID', value: 'alertRuleId'},
          {text: 'Name', value: 'alertRuleName'},
          {text: 'Actions', sortable: false}
        ]
      }
    },
    async asyncData({app}) {
      const notifications = await app.$axios.$get('api/notifications');
      return {notifications}
    },
    methods: {
      deleteNotification(id) {
        this.$axios.$delete(`api/notifications/${id}`).then(res => {
          alert('deleted!');
          this.$router.push("/alert/notification")
        })
      }
    }
  }
</script>

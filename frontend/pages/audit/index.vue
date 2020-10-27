<template>
  <v-layout
    row
    wrap>
    <v-flex>
      <v-card>
        <v-card-title>
          Crucial Action Audit
        </v-card-title>
        <v-data-table
          :headers="headers"
          :items="audit"
          hide-actions>
          <template
            slot="items"
            slot-scope="props">
            <td>{{props.item.performedByName}}</td>
            <td>{{props.item.actionPerformed}}</td>
            <td>{{props.item.targetName}}</td>
            <td>{{props.item.timestamp}}</td>
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
          {text: 'User', value: 'userId'},
          {text: 'Action performed', value: 'crud'},
          {text: 'Target', value: 'targetEntity'},
          {text: 'Timestamp', value: 'timestamp'}
        ]
      }
    },
    async asyncData({app}) {
      const audit = await app.$axios.$get('api/audit')
      return {audit: audit}
    },
  }
</script>

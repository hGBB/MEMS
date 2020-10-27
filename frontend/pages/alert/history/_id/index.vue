<template>
  <v-layout
    row
    wrap>
    <v-flex>
      <notification
        v-model="notification">
      </notification>
    </v-flex>
  </v-layout>
</template>
<script>
  import notification from '@/components/notification'

  export default {
    components: {
        notification
      },

      async asyncData({app, params}) {
      const notification = await app.$axios.$get(`/api/notifications/${params.id}`)
      return {notification}
    },
  }
</script>

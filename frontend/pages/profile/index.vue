<template>
  <v-card>
    <user-details v-model="userProfile"></user-details>
  </v-card>
</template>

<script>
  import Profile from '@/components/profile'

  export default {
    components: {
      'userDetails': Profile
    },
    async asyncData ({app}) {
      const user = await app.$axios.$get('api/users/self');
      return {user}
    },
    computed: {
      userProfile () {
        return this.user
      }
    }
  }

</script>

<style scoped>

</style>

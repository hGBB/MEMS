<template>
  <v-app>
    <!-- Main content -->
    <v-content>
      <v-container>
        <nuxt/>
        <toast></toast>
      </v-container>
    </v-content>
    <v-footer
      app
    >
      <span>&copy; 2018. Made with 	&#x2764; by Team Hams.</span>
    </v-footer>
  </v-app>
</template>

<script>
  import Toast from '@/components/toast'

  export default {
    components: {
      Toast
    }
  }
</script>

<style scoped>

</style>

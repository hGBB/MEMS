<template>
  <v-layout column wrap>
    <v-flex xs12>
      <user
        v-model="user"
      >

      </user>
    </v-flex>
    <v-layout justify-end>
      <v-btn
        color="green" dark fab large
        @click="save"><v-icon>done</v-icon>
      </v-btn>
    </v-layout>
  </v-layout>
</template>

<script>
  import User from '@/components/user'

  export default {
    components: {
      User
    },
    data () {
      return {
        user: {}
      }
    },
    methods: {
      save () {
        this.$axios.$post('api/users', this.user).then(res => {
          alert('saved!')
          this.$router.push("/users")
        })
      }
    }
  }
</script>

<style scoped>

</style>

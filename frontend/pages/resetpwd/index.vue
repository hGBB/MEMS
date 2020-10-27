<template>
  <v-layout align-center justify-center row wrap>
    <v-flex md6 xs12>
      <v-card>
        <v-card-title class="headline">
          Reset your Password
        </v-card-title>
        <v-card-text>
          <v-text-field label="Username"
                        v-model="user.login"
          />
          <v-text-field label="Email"
                        v-model="user.email"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn @click="reset"
                 color="primary"
                 flat
          >
            Send reset Email
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    layout: 'none',
    mounted () {
    },
    data () {
      return {
        user: {}
      }
    },
    methods: {
      async reset () {
        this.$axios.$post('api/users/resetpwd', this.user).then(res => {
          alert('Email sent!')
        })
      }
    }
  }
</script>

<style scoped>

</style>


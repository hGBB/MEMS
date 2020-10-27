<template>
  <v-layout align-center justify-center row wrap>
    <v-flex md6 xs12>
      <v-card>
        <v-card-title class="headline">
          Set new Password
        </v-card-title>
        <v-card-text>
          <v-text-field label="Password"
                        type="password"
                        v-model="password"
          />
          <v-text-field label="Confirm Password"
                        type="password"
                        v-model="confirm"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn @click="newpwd"
                 :disabled="!this.passwordsAcceptable"
                 color="primary"
                 flat
          >
            Test new Password
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
        password: '',
        confirm: ''
      }
    },
    methods: {
      async newpwd () {
        this.$axios.$post(`api/users/newpwd?token=${this.$route.query.token}`,
          {changedPassword: this.password}).then((res) => {this.$router.push('/login')})
      }
    },
    computed: {
      passwordsAcceptable () {
        return this.password === this.confirm && this.password !== ""
      }
    }
  }
</script>

<style scoped>

</style>

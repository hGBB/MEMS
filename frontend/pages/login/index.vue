<template>
  <v-layout align-center justify-center row wrap>
    <v-flex md6 xs12>
      <v-card>
        <v-card-title class="headline">
          <v-icon>lock</v-icon>
          Tickets, please!
        </v-card-title>
        <v-form @submit.stop.prevent="doLogin">
          <v-card-text>
            <v-text-field autofocus
                          label="Username"
                          v-model="userdata.username"/>
            <v-text-field label="Password"
                          type="password"
                          v-model="userdata.password"
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer/>
            <v-btn color="primary"
                   flat
                   type="submit">
              Login
            </v-btn>
            <nuxt-link to="/resetpwd">Forgot Password?</nuxt-link>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  import {mapActions} from 'vuex'
  import {LEVEL} from '@/store/message'

  export default {
    layout: 'none',
    data () {
      return {
        userdata: {}
      }
    },
    methods: {
      ...mapActions({
        login: 'auth/login',
        sendMessage: 'message/sendMessage'
      }),
      async doLogin () {
        if (!this.userdata.username || !this.userdata.password) {
          this.sendMessage({
            message: 'Login failed! Check username/password.',
            level: LEVEL.WARNING
          });
          return
        }
        await this.login(this.userdata).catch(err => {
          this.sendMessage({message: 'Login failed! Check username/password.', level: LEVEL.WARNING})
        });
        this.$router.push('/')
      }
    }
  }
</script>

<style scoped>

</style>

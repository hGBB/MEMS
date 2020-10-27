<template>
  <v-card class="user">
    <v-card-title class="headline">
      User Data
    </v-card-title>
    <v-card-text>
      <v-layout column wrap>
        <v-flex
          xs12>
          <v-text-field
            label="Username"
            v-model="value.login"/>

        </v-flex>
        <v-flex
          xs12>
          <v-text-field
            label="Full Name"
            v-model="value.name"/>

        </v-flex>
        <v-layout row>
          <v-flex xs6 mr-1>
            <v-text-field
              label="E-Mail"
              v-model="value.email"/>
          </v-flex>
        </v-layout>
        <v-layout row v-if="!create">
          <v-flex xs6 mr-1>
            <v-text-field
              label="Password"
              :error="!passwordsMatch"
              :append-icon="show1 ? 'visibility_off' : 'visibility'"
              :type="show1 ? 'text' : 'password'"
              @click:append="show1 = !show1"
              v-model="value.changedPassword"/>
          </v-flex>
          <v-flex xs6 ml-1>
            <v-text-field
              label="Confirm Password"
              :error="!passwordsMatch"
              :error-messages="!passwordsMatch ? ['Confirm Password does not match!'] : []"
              :append-icon="show1 ? 'visibility_off' : 'visibility'"
              :type="show1 ? 'text' : 'password'"
              @click:append="show1 = !show1"
              v-model="confirmPassword"/>
          </v-flex>
        </v-layout>
        <v-flex xs12>
          <v-layout column wrap>
            <v-flex xs12><h3>Type</h3></v-flex>
            <v-flex xs12>
              <v-radio-group
                column
                v-model="authorities">
                <v-radio
                  label="Basic"
                  value="basic"/>
                <v-radio
                  label="Privileged"
                  value="priv"></v-radio>
                <v-radio
                  label="Administrator"
                  value="admin"
                ></v-radio>
              </v-radio-group>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
    </v-card-text>
  </v-card>
</template>
<script>
  export default {
    props: {
      'value': Object,
      create: ''
    },
    watch: {
      value: {
        handler (nv, ov) {
          this.$emit('input', nv)
        },
        deep: true
      }
    },
    data () {
      return {
        auth: '',
        confirmPassword: '',
        confirmEmail: '',
        show1: false
      }
    },

    computed: {
      authorities: {
        get () {
          if (this.value.admin) {
            return 'admin'
          } else if (this.value.privileged) {
            return 'priv'
          }
          return 'basic'
        },
        set (nv) {
          this.auth = nv;
          if (nv === 'basic') {
            this.value.admin = false;
            this.value.privileged = false
          } else if (nv === 'priv') {
            this.value.admin = false;
            this.value.privileged = true
          } else if (nv === 'admin') {
            this.value.admin = true;
            this.value.privileged = false
          }
        }
      },
      passwordsMatch () {
        return this.value.changedPassword === this.confirmPassword
      },
    },
  }

</script>
<style scoped>
</style>

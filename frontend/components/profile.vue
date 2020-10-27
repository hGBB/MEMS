<template>
  <v-layout row wrap>
    <v-flex pa2 sm12>
      <v-card class="user">
        <v-card-title class="headline">
          User Data
        </v-card-title>
        <v-card-text>
          <v-layout column wrap>
            <v-layout row>
              <v-flex
                xs6>
                <v-text-field
                  label="Full Name"
                  v-model="value.name"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs6 mr-1>
                <v-text-field
                  label="E-Mail"
                  v-model="value.email"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex
                xs6>
                <v-text-field
                  label="Old Password"
                  :error="!passwordsMatch"
                  :append-icon="show1 ? 'visibility_off' : 'visibility'"
                  :type="show1 ? 'text' : 'password'"
                  @click:append="show1 = !show1"
                  v-model="oldPassword"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs6 mr-1>
                <v-text-field
                  label="Password"
                  :error="!passwordsMatch"
                  :append-icon="show2 ? 'visibility_off' : 'visibility'"
                  :type="show2 ? 'text' : 'password'"
                  @click:append="show2 = !show2"
                  v-model="newPassword"/>
              </v-flex>
              <v-flex xs6 ml-1>
                <v-text-field
                  label="Confirm Password"
                  :error="!passwordsMatch"
                  :error-messages="!passwordsMatch ? ['Confirm Password does not match!'] : []"
                  :append-icon="show2 ? 'visibility_off' : 'visibility'"
                  :type="show2 ? 'text' : 'password'"
                  @click:append="show2 = !show2"
                  v-model="confirmPassword"/>
              </v-flex>
            </v-layout>
          </v-layout>
        </v-card-text>
      </v-card>
    </v-flex>
    <v-layout justify-end>
      <v-dialog
        v-model="dialog"
        width="300"
      >
        <v-btn
          slot="activator"
          color="green" dark fab large
          @click="acceptableChanges"
        >
          <v-icon>done</v-icon>
        </v-btn>
        <v-card>
          <v-card-text style="font-size: large">{{popupmsg}}</v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              flat
              @click="dialog = false"
            >
              OK
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>
  </v-layout>
</template>
<script>
  export default {
    props: {
      'value': Object
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
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
        confirmEmail: '',
        show1: false,
        show2: false,
        popupmsg: '',
        dialog: false
      }
    },
    computed: {
      passwordsMatch () {
        return this.newPassword === this.confirmPassword
      }
    },
    methods: {
      save () {
        this.value.oldPassword = this.oldPassword;
        this.$axios.$put(`api/users/${this.value.id}`, this.value)
      },
      knowsOldPassword () {
        return true
      },
      correctPassword () {
        return this.newPassword === this.confirmPassword
      },
      acceptableChanges () {
        if (this.oldPassword === '') {
          if (this.newPassword === '' && this.correctPassword()) {
            this.popupmsg = 'Changes saved.';
            this.save()
          } else {
            this.popupmsg = 'To change the password you need your current password,' +
              'and the new password needs to be confirmed!'
          }
        } else if (this.knowsOldPassword()) {
          if (this.newPassword === '' && this.correctPassword()) {
            this.popupmsg = 'Changes saved. ' +
              'There is no need to name your current password to change your eMail address.';
            this.save()
          } else if (this.correctPassword()) {
            this.popupmsg = 'Changes saved.';
            this.value.changedPassword = this.newPassword;
            this.save()
          } else {
            this.popupmsg = 'New Password and Confirm Password do not match!'
          }
        } else if (!this.knowsOldPassword()) {
          if (this.newPassword === '' && this.correctPassword()) {
            this.popupmsg = 'Changes saved.';
            this.save()
          } else {
            this.popupmsg = 'Wrong old password!'
          }
        }
      }
    }
  }

</script>
<style scoped>
</style>

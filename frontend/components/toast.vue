<template>
  <v-snackbar
    :color="msg && msg.level"
    :timeout="3000"
    :value="msg"
    @input="onSnackToggled">
    {{ msg && msg.message }}
  </v-snackbar>
</template>

<script>
  import {mapActions, mapGetters} from 'vuex'

  export default {
    computed: {
      ...mapGetters({
        msg: 'message/getNextMessage'
      })
    },
    data () {
      return {
        snack: false
      }
    },
    methods: {
      ...mapActions({
        nextMessage: 'message/nextMessage'
      }),
      onSnackToggled (nv) {
        this.snack = nv
        if (!nv) {
          setTimeout(() => {
            this.nextMessage()
          }, 500)
        }
      }
    }
  }
</script>

<style scoped>

</style>

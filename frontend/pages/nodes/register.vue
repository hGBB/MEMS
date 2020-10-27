<template>
  <v-layout
    my-2
    row
    wrap>
    <v-flex sm12>
      <v-card>
        <v-card-title class="headline">
          Register Node
        </v-card-title>
        <v-card-text>
          <p>
            Please enter the PIN displayed below, when following the steps to install the Monitoring Client.
            The PIN is valid for {{pin.validity}} seconds.
          </p>
          <p>
            Feedback about the successful connection of your machine will be displayed there. You may leave
            this page after application of the PIN.
          </p>
          <v-progress-circular
            :value="remainingValidity"
            color="primary"
          ></v-progress-circular>
          {{pin.value}}
        </v-card-text>
        <v-card-actions>
          <v-btn @click="$router.push('/nodes')" color="success">Back</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  function setTimer (app) {
    let sub = 100 / app.pin.validity

    function callback () {
      app.remainingValidity -= sub
      if (app.remainingValidity > 0) {
        app.timer = window.setTimeout(callback, 1000)
      } else {
        app.nextPin()
      }
    }

    app.remainingValidity = 100
    app.timer = window.setTimeout(callback, 1000)
  }

  export default {
    async asyncData ({app}) {
      const pin = await app.$axios.$get('api/nodes/pin')
      return {pin}
    },
    mounted () {
      setTimer(this)
    },
    data () {
      return {
        pin: {},
        timer: null,
        remainingValidity: 100
      }
    },
    watch: {
      pin: {
        deep: true,
        handler (nv) {
          window.clearTimeout(this.timer)
          setTimer(this)
        }
      }
    },
    methods: {
      async nextPin () {
        this.pin = await this.$axios.$get('api/nodes/pin')
      }
    },
    beforeRouteLeave (to, from, next) {
      window.clearTimeout(this.timer)
      next()
    }
  }
</script>

<style scoped>

</style>


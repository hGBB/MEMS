<template>
  <v-layout
    column
    wrap
  >
    <v-flex
      pa-2
      sm6
    >
      <inputForm
        v-model="alertRule"
        v-bind:kpis="kpi"
        v-bind:create="false"
        v-bind:nodes="node">
      </inputForm>
    </v-flex>
    <v-flex>
      <v-btn
        @click="editAr"
        color="warning"
      >Update
      </v-btn>
      <v-btn click="deleteAr"
             color="error">Delete
      </v-btn>
    </v-flex>
  </v-layout>
</template>

<script>
  import inputForm from '@/components/inputForm'

  export default {
    components: {
      inputForm
    },

    async asyncData({app, params}) {
      const alertRule = await app.$axios.$get(`api/alerts/${params.id}`);
      const kpi = await app.$axios.$get(`api/kpi`);
      const node = await app.$axios.$get("/api/nodes");
      return {alertRule, kpi, node}
    },
    methods: {

      editAr() {
        this.$axios.put(`api/alerts/${this.alertRule.id}`, this.alertRule).then(res => {
          alert('updated!');
          this.$router.push("/alert")
        })
      },
      async deleteAr() {
        await this.$axios.$delete(`api/alerts/${this.alertRule.id}`).then(res => {
          alert('deleted!');
          this.$router.push("/alert")
        })
      }
    }

  }
</script>


<template>
  <v-layout column wrap>
    <v-flex pa-2 sm12>
      <group :nodes="nodes" :users="users" v-model="group"></group>
    </v-flex>
    <v-flex>
      <v-btn @click="save" color="success">Save</v-btn>
    </v-flex>
  </v-layout>
</template>

<script>
  import Group from '@/components/group'

  export default {
    components: {Group},
    async asyncData ({app, params}) {
      const group = await app.$axios.$get(`api/groups/${params.id}`)
      const users = await app.$axios.$get(`api/users`)
      const nodes = await app.$axios.$get(`api/nodes`)
      return {group, users, nodes}
    },
    methods: {
      save () {
        this.$axios.$put(`api/groups/${this.group.id}`, this.group).then(res => {
          alert('saved!')
          this.$router.push('/groups')
        })
      }
    }
  }
</script>



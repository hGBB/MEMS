<template>
  <v-layout column wrap>
    <v-flex pa-2 sm12>
      <group :nodes="nodes" :users="users" v-model="group"></group>
    </v-flex>
    <v-flex>
      <v-btn @click="create" color="success">Save</v-btn>
    </v-flex>
  </v-layout>
</template>

<script>
  import Group from '@/components/group'

  export default {
    components: {Group},
    async asyncData ({app, params}) {
      const users = await app.$axios.$get(`api/users`);
      const nodes = await app.$axios.$get(`api/nodes`);
      return {users, nodes}
    },
    data () {
      return {
        group: {}
      }
    },
    methods: {
      create () {
        this.$axios.$post('api/groups', this.group).then(res => {
          alert('saved!');
          this.$router.push("/groups")
        })
      }
    }
  }
</script>



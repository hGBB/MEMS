<template>
  <v-card>
    <v-card-title>
      <h3 class="headline mb-0">Group</h3>
    </v-card-title>
    <v-card-text>
      <v-text-field label="Name" v-model="value.name"></v-text-field>
      <v-layout row>
        <v-flex xs6>
          <v-select :items="users"
                    chips deletable-chips item-text="name" item-value="id"
                    label="Users" multiple v-model="gUser"></v-select>
        </v-flex>
        <v-flex xs6>
          <v-select :items="nodes"
                    chips deletable-chips item-text="name" item-value="id"
                    label="Nodes" multiple v-model="gNode"></v-select>
        </v-flex>
      </v-layout>
    </v-card-text>
  </v-card>
</template>
<script>
  export default {
    props: {
      value: Object,
      users: Array,
      nodes: Array
    },
    computed: {
      gUser: {
        get () {
          if (!this.value.users) return []
          return this.value.users.map(u => u.id)
        },
        set (ids) {
          this.value.users = ids.map(id => ({id}))
        }
      },
      gNode: {
        get () {
          if (!this.value.nodes) return []
          return this.value.nodes.map(u => u.id)
        },
        set (ids) {
          this.value.nodes = ids.map(id => ({id}))
        }
      }
    },
    watch: {
      'value': {
        deep: true,
        handler (nv) {
          this.$emit('input', nv)
        }
      }
    },
    methods: {}
  }

</script>
<style scoped>
</style>

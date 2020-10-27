<template>
  <v-card class="rule">
    <v-btn
      small
      color="warning"
      @click="editAR"
    >
      <v-icon dark> edit</v-icon>
    </v-btn>
    <v-btn
      small
      color="error"
      @click="deleteAR"
    >
      <v-icon dark> delete</v-icon>
    </v-btn>
    <h1> Name:  {{ name }} ID: {{ id }}</h1>
    <p>Oscillation Occurrence Count:  {{ occ }}</p>
    <p>Oscillation Time Frame:  {{ otf }}</p>
    <p>Oscillation Sleep: {{ sleep }}</p>
  </v-card>
</template>
<script>
  export default {
    props: {
      id: '',
      name: '',
      occ: '',
      otf: '',
      sleep: '',
      userId: ''
    },
    methods: {
      editAR() {
        this.$emit('edit', this.id)
      },
      deleteAR() {
        this.$emit('delete', this.id)
      }
    }
  }

</script>
<style scoped>
  .rule {
    box-sizing: border-box;
    width: 280px;
    padding: 8px;
    margin: 10px;
    border: 1px solid #ccc;
    box-shadow: 0 2px 2px #aaa;
  }
</style>

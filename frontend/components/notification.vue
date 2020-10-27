<template>
  <v-layout
    row
    wrap>
    <v-card>
      <v-card-title>
        <h3 class="headline">Notification</h3>
      </v-card-title>
      <v-card-text>
        Occured: {{value.timestamp}}
      </v-card-text>
      <v-card-text>
        Message: {{value.text}}
      </v-card-text>
      <v-card-title><h3> Rule Triggered: </h3></v-card-title>
      <v-card-text>
        Alert Rule ID: {{value.alertRule}}
      </v-card-text>
      <v-card-text>
        Alert Rule Name: {{value.alertRuleName}}
      </v-card-text>

    </v-card>
  </v-layout>

</template>
<script>
  export default {
    props: {
      value: Object
    }
  }
</script>

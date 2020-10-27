<template>
  <line-chart
    :chart-data="this.datacollection" :options="options">
  </line-chart>
</template>

<script>
  import LineChart from './LineChart.js'
  export default {
    components: {
      LineChart
    },
    props: {
      node: Object,
      kpi: Object,
      records: {},
      recordsOnNode: {}

    },
    data () {
      return {
        options: {

          scales: {
            xAxes: [{
              type: 'time',
              time: {
                min: this.node.start,
                max:this.node.end,
                displayFormats: {
                  day: 'YYYY MM DD'
                }
              },
              distribution: 'linear'
            }],
            yAxes: [{
              scaleLabel: {
                display: true,
                labelString: this.kpi.unit
              }
            }]
          },
          elements: {
            line: {
              fill: false,
              borderColor: 'green',
              borderWidth: 1,
              tension: 0
            },
          },
          legend: {
            display: false
          },
          responsive: true,
          maintainAspectRatio: false
        },
      }
    },

    computed: {
      datacollection: {
        get() {
          var dataDisplayed = []
          var labelDisplayed = []
          var i = 0
          for (i = 0; i < this.recordsOnNode.length; i++) {
            if (this.recordsOnNode[i].type.id === this.kpi.id) {
              var live = new Date()
              live.setMinutes(live.getMinutes() - this.node.lastX)
              var timestamp = this.recordsOnNode[i].timestamp.toString()
              var stampDate = new Date(
                parseInt(timestamp.substr(0, 4)),
                parseInt(timestamp.substr(5, 2)),
                parseInt(timestamp.substr(8, 2)),
                parseInt(timestamp.substr(11, 2)),
                parseInt(timestamp.substr(14, 2)),
                parseInt(timestamp.substr(17, 2)),
                parseInt(timestamp.substr(20, 3)))
              var historyStartString = this.node.historyStart.toString()
              var historyStart = new Date()
              historyStart.setFullYear(parseInt(historyStartString.substr(0, 4)), parseInt(historyStartString.substr(5, 2)), parseInt(historyStartString.substr(8, 2)))
              historyStart.setHours(parseInt(historyStartString.substr(11, 2)) - 1)
              historyStart.setMinutes(parseInt(historyStartString.substr(14, 2)))
              historyStart.setSeconds(parseInt(historyStartString.substr(11, 2)))
              historyStart.setMilliseconds(parseInt(historyStartString.substr(20, 3)))

              var historyEndString = this.node.historyEnd.toString()
              var historyEnd = new Date()
              historyEnd.setFullYear(parseInt(historyEndString.substr(0, 4)), parseInt(historyEndString.substr(5, 2)), parseInt(historyEndString.substr(8, 2)))
              historyEnd.setHours(parseInt(historyEndString.substr(11, 2)) - 1)
              historyEnd.setMinutes(parseInt(historyEndString.substr(14, 2)))
              historyEnd.setSeconds(parseInt(historyEndString.substr(11, 2)))
              historyEnd.setMilliseconds(parseInt(historyEndString.substr(20, 3)))
              if (this.node.mode === 'live' && this.node.lastX > 0) {
                stampDate.setMonth(stampDate.getMonth() - 1)
                stampDate.setHours(stampDate.getHours() + 1)
                if (live < stampDate) {
                  dataDisplayed.push(this.recordsOnNode[i].value);
                  labelDisplayed.push(this.recordsOnNode[i].timestamp);
                }
              } else if (this.node.mode === 'hist') {
                if (historyStart < stampDate && stampDate < historyEnd) {
                  dataDisplayed.push(this.recordsOnNode[i].value);
                  labelDisplayed.push(this.recordsOnNode[i].timestamp);
                }
              }
            }
          }
          return {
            labels: labelDisplayed,
            datasets: [
              {
                label: this.kpi.name,
                data: dataDisplayed
              }
            ]
          }
        }
      }

    }
  }
</script>

<style>

</style>

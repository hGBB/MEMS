import Vue from 'vue'
import {Client} from '@stomp/stompjs'

/**
 * Sends a message including the provided dashboard details,
 * in order to reconfigure the dashboard.
 *
 * @param dashboard Refer to DashboardDTO at BE.
 */
Client.prototype.configureDashboard = function (dashboard) {
  this.publish({destination: '/fe/configure', body: JSON.stringify(dashboard)})
};



/**
 * Proudly brought to you by Vue ;) Mind the gap folks.
 *
 * @type {{install: Stomp.install}}
 */
const Stomp = {
  install: function (Vue) {

    let dashboard;

    /**
     * Connect the dashboard to the BE and subscribe to fresh data.
     *
     * @param brokerURL The BE's message broker URL.
     * @param receive Callback to invoke when updates come through.
     * @returns {Promise} Fulfilled when connection is established.
     */
    Vue.prototype.$stompConnect = function (brokerURL, receive) {
      return new Promise((res, rej) => {
        let c = new Client({
          brokerURL,
          debug: (str) => console.debug(str)
        });
        c.onConnect = (frame) => {
          console.debug('connected');
          dashboard = c.subscribe(`/user/topic/dashboard`, m => {
            receive(m.body)
          });
          res(c)
        };
        c.onStompError = (frame) => {
          console.warn('Broker reported error: ' + frame.headers['message']);
          console.warn('Additional details: ' + frame.body);
          console.warn('will reject promise');
          rej('WS connect error')
        };
        c.activate()
      })
    }

  }
};

Vue.use(Stomp);

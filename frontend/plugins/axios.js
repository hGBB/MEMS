import {LEVEL} from '@/store/message'

export default function ({$axios, store, redirect}) {

  $axios.onRequest(config => {
  });

  $axios.onError(error => {
    const code = parseInt(error.response && error.response.status);
    if (code === 401) {

      $axios.$post('auth/refresh').then(res => {

        $axios(error.request).then(resres => {
          return Promise.resolve(resres)
        }).catch(errerr => {
          return Promise.reject(errerr)
        })
      })
    } else {
      if (!(code === 403)) {
        store.dispatch('message/sendMessage', {message: `Request failed! (${code})`, level: LEVEL.ERROR})
      }
    }
  })
}

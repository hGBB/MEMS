export const state = () => ({
  messageQueue: []
})

export const mutations = {
  enqueueMessage (state, msg) {
    state.messageQueue.push(msg)
  },
  dequeueMessage (state) {
    state.messageQueue.shift()
  }
}

export const getters = {
  getNextMessage (state) {
    if (state.messageQueue.length > 0) {
      return state.messageQueue[0]
    }
    return null
  }
}

export const actions = {
  nextMessage ({commit}) {
    commit('dequeueMessage')
  },
  sendMessage ({commit}, {message, level}) {
    if (Object.values(LEVEL).indexOf(level) > -1 && message) {
      commit('enqueueMessage', {message, level})
    } else {
      throw {message: 'Message empty or invalid level.'}
    }
  }
}

export const LEVEL = {
  INFO: 'info',
  SUCCESS: 'success',
  WARNING: 'warning',
  ERROR: 'error'
}

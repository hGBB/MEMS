export const state = () => ({
  principal: {},
  authenticated: false,
  privileged: false,
  admin: false
});

export const mutations = {
  loginSuccess (state, payload) {
    state.principal = payload;
    state.authenticated = true
  },
  setAuthorities (state, payload) {
    state.privileged = payload.privileged;
    state.admin = payload.admin
  },
  logoutSuccess (state) {
    state = {}
  }
};

export const getters = {
  getUserInfo (state) {
    return state
  }
};

export const actions = {
  async login ({commit}, payload) {
    const loginRes = await this.$axios.post('auth/login', payload).catch(err => {
      throw err
    });
    commit('loginSuccess', {login: payload.username});
    const userInfo = await this.$axios.$get(`api/users/self`);
    commit('setAuthorities', {privileged: userInfo.privileged, admin: userInfo.admin})
  },
  async logout ({commit}) {
    await this.$axios.$post('/auth/logout');
    commit('logoutSuccess')
  }
};


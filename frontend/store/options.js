export const state = () => ({
  optionsOpen: false
})

export const mutations = {
  toggle (state) {
    state.optionsOpen = !state.optionsOpen
  },
  setOpen (state, open) {
    state.optionsOpen = open
  }
}

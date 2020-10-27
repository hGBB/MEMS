export default function ({store, redirect, route}) {
  const u = store.getters['auth/getUserInfo']
  if (!u.authenticated) {
    return redirect('/login')
  }
}

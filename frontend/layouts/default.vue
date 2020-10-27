<template>
  <v-app>
    <v-navigation-drawer
      :mini-variant="miniVariant"
      v-model="drawer"
      clipped
      fixed
      app
    >
      <v-list>

        <div
          v-for="(m, i) in menu"
          :key="i"
          v-if="!m.authorities || m.authorities[authority]">
          <v-subheader
            inset>{{ m.name }}
          </v-subheader>

          <v-list-tile
            v-for="(item, i) in m.items"
            :to="item.to"
            v-if="!item.authorities || item.authorities[authority]"
            :key="i"
            router
            exact
          >
            <v-list-tile-action>
              <v-icon v-html="item.icon"/>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title v-text="item.title"/>
            </v-list-tile-content>
          </v-list-tile>
        </div>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar
      clipped-left
      fixed
      app
    >
      <v-btn
        icon
        @click="drawer = !drawer"
      >
        <v-icon>menu</v-icon>
      </v-btn>

      <v-btn
        icon
        @click.stop="miniVariant = !miniVariant"
      >
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"/>
      </v-btn>

      <v-toolbar-title>&#x1F356; HamerMEMS</v-toolbar-title>
      <v-spacer/>
      <v-toolbar-items>
        <v-menu>
          <v-btn
            slot="activator"
            flat>
            <v-icon>person</v-icon>
            Profile
          </v-btn>
          <v-list>
            <v-list-tile @click="$router.push('/profile')">
              <v-list-tile-title>
                <v-icon>person_outline</v-icon>
                Edit Profile
              </v-list-tile-title>
            </v-list-tile>
            <v-list-tile @click="logoff">
              <v-list-tile-title>
                <v-icon>power_settings_new</v-icon>
                Log off
              </v-list-tile-title>
            </v-list-tile>
          </v-list>
        </v-menu>
        <v-btn
          icon
          @click="toggle">
          <v-icon>more_vert</v-icon>
        </v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <!-- Main content -->
    <v-content>
      <v-container>
        <nuxt/>
        <toast></toast>
      </v-container>
    </v-content>

    <!--
    -->
    <v-footer
      app
    >
      <span>&copy; 2018. Made with 	&#x2764; by Team Hams.</span>
    </v-footer>
  </v-app>
</template>

<script>
  import {mapMutations} from 'vuex'
  import Toast from '@/components/toast'

  export default {
    middleware: 'auth',
    components: {
      Toast
    },
    data () {
      return {
        clipped: false,
        drawer: true,
        fixed: false,
        menu: [
          {
            name: 'Monitoring',
            items: [
              {icon: 'insert_chart_outlined', title: 'Dashboard', to: '/'},
              {icon: 'notification_important', title: 'Alert Rules', to: '/alert'},
            ]
          },
          {
            name: 'Administration',
            authorities: {admin: true, priv: true, user: false},
            items: [
              {icon: 'group_add', title: 'Group Management', to: '/groups', authorities: {admin: true, priv: true}},
              {icon: 'settings', title: 'Node Settings', to: '/nodes', authorities: {admin: true, priv: true}},
              {icon: 'people', title: 'User Management', to: '/users', authorities: {admin: true, priv: true}},
              {icon: 'book', title: 'Audit', to: '/audit', authorities: {admin: true, priv: false}},
            ]
          }
        ],
        miniVariant: false,
        right: true,
        title: ''
      }
    },
    methods: {
      ...mapMutations({toggle: 'options/toggle'}),
      async logoff () {
        await this.$axios.$post('/auth/logout')
        this.$router.push('/login')
      }
    },
    computed: {
      authority () {
        if (this.$store.getters['auth/getUserInfo'] && this.$store.getters['auth/getUserInfo'].admin) {
          return 'admin'
        }
        if (this.$store.getters['auth/getUserInfo'] && this.$store.getters['auth/getUserInfo'].privileged) {
          return 'priv'
        }
        return 'user'
      }
    }
  }
</script>

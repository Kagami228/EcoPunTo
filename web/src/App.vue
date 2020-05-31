<template>
  <div id="app">
    <b-container fluid class="h-100">
      <b-row class="h-100">
        <b-col cols="2" class="h-100"> 
          <b-card class="h-100"
            header="Мусорки" >
              <div v-for="bin in state" :key="bin.id">
                <h5>
                  <b-badge  class="m-1" :variant="bin.event_type=='Full'?'danger':'success'">{{bin.id}}</b-badge>
                  <b-badge class="m-1">{{bin.event_type}}</b-badge>
                </h5>


              </div> 
          </b-card>
        </b-col>
        <b-col cols="10">
          <pets-map
            id="pets-map"
            apiKey="oELmZuklvv_LuXrFZ6zML1zYaomNB9FERQMTibCw0Sw"
            appCode="gXBknUXDXqyq7Cd4v0ca"
            width="100vw"
            height="100vh"
            zoom="10"
            lat="46.3032133"
            lng="30.4951133"
          />
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import PetsMap from "./components/HereMap";
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState({
      state: (state) => state.state,
    }),
    position() {
      return {
        lat: this.currentData.payload_fields.lat,
        lng: this.currentData.payload_fields.long,
      };
    },
    vbatt() {
      return this.currentData.payload_fields.vbatt;
    },
  },
  name: "app",
  components: {
    PetsMap,
  },
};
</script>

<style>
html,
body {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
body {
  margin: 0;
}
#app {
  margin: 10px;

  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: calc(100% - 20px);
  display: flex;
  flex-flow: column;
  height: 100%;
}
#info {
  flex: 0 1 auto;
  padding: 0;
}
#pets-map {
  flex: 1 1 auto;
}
.badge {
  display: inline;
  margin: 0 5px;
}
</style>

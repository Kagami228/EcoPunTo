<template>
  <div class="here-map">
    <div ref="map" v-bind:style="{ width: width, height: height }"></div>
  </div>
</template>

<script>
/*global H*/

export default {
  name: "HereMap",
  data() {
    return {
      platform: {},
      map: {},
      ui: {},
      mapEvents: {},
      behavior: {},
      timer: null,
      groupInited: null,
      groupFull: null,
    };
  },

  props: {
    apiKey: String,
    appCode: String,
    width: String,
    height: String,
    lat: String,
    lng: String,
    zoom: String,
  },
  methods: {
    initDbConnection() {
      this.$db.collection("state").onSnapshot((snapshot) => {
        console.log(
          "Current state: ",
          snapshot.docs.map((d) => d.data())
        );

        this.$store.commit(
          "setState",
          snapshot.docs.map((d) => d.data())
        );
        if (this.groupFull != null) this.map.removeObject(this.groupFull);

        this.groupFull = new H.map.Group();
        this.map.addObject(this.groupFull);
        snapshot.docs
          .map((d) => d.data())
          .forEach((d) => {
            var empty = new H.map.Icon("img/trash.png", {
              size: { w: 30, h: 30 },
            });
          
          var full = new H.map.Icon("img/trashoff.png", {
              size: { w: 21, h: 28 },
            });
            var marker = new H.map.Marker(
              { lat: d.lat, lng: d.long },
              { icon: d.event_type=="Full"?full:empty }
            );
            this.groupFull.addObject(marker);
          });
      });

      // this.$db
      //   .collection("events")
      //   .where("event_type", "==", "Initilize")
      //   .onSnapshot((snapshot) => {
      //     console.log(
      //       "Current data: ",
      //       snapshot.docs.map((d) => d.data())
      //     );
      //     this.$store.commit(
      //       "setInited",
      //       snapshot.docs.map((d) => d.data())
      //     );

      //     if (this.groupInited != null) this.map.removeObject(this.group);

      //     this.groupInited = new H.map.Group();
      //     this.map.addObject(this.groupInited);

      //     snapshot.docs
      //       .map((d) => d.data())
      //       .forEach((d) => {
      //         var marker = new H.map.Marker({ lat: d.lat, lng: d.long });
      //         this.groupInited.addObject(marker);
      //       });
      //   });
    },
    initMap() {
      var layers = this.platform.createDefaultLayers();
      this.map = new H.Map(this.$refs.map, layers.vector.normal.map, {
        zoom: this.zoom,
        center: { lng: this.lng, lat: this.lat },
      });

      this.ui = H.ui.UI.createDefault(this.map, layers);
      this.mapEvents = new H.mapevents.MapEvents(this.map);
      //var icon = new H.map.Icon('trash.png');

      // Instantiate the default behavior, providing the mapEvents object:
      this.behavior = new H.mapevents.Behavior(this.mapEvents);
    },
  },
  created() {
    this.platform = new H.service.Platform({
      apikey: this.apiKey,
    });
  },

  mounted() {
    console.log("sadsasad");
    this.initMap();
    this.initDbConnection();
  },
};
</script>

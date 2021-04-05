<template>
    <div>
        <div id="myChart" :style="{ width: '800px', height: '300px' }"></div>
        <transition  name="el-fade-in"><div v-if="showIV"><timeIV @isClosed="closeDeatil" /></div></transition>
    </div>

</template>
<style>
</style>
<script>
import timeIV from '@/components/timeIV'
export default {
  name: 'index',
  components: {
    timeIV,
  },
  data() {
    return{
      showIV: true,
    }
  },
  methods:{
    closeDeatil(flag){
      this.showIV = false;
    },

    initEChart(){
      var _this=this;
        //this.$root => app
      let myChart = this.$root.echarts.init(
        document.getElementById("myChart")
      );
      myChart.on('click',function(params){
        _this.showIV = true;
      });
      // 绘制图表
      myChart.setOption({
        title: { text: "板1" },
        tooltip: {},
        legend: {
          show:false,
        },
        xAxis: {
          boundaryGap: false,
          data: ["12-3", "12-4", "12-5", "12-6", "12-7", "12-8"],
        },
        yAxis: {
          boundaryGap: false,
          
          data: [" ","正常","开路","短路","阴影",""],
        },
        series: [
          {
            name: "板1",
            type: "line",
            data: [0, 1, 2, 3, "阴影", 0],
          },
        ],
        backgroundColor: 'rgba(255,255,255,0.8)',
      });
    },

  },
  mounted() {
    this.showID=true;
    this.initEChart();

  },
}
</script>


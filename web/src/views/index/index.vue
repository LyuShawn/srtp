<template>
    <div>
        <div id="myChart" :style="{ width: '100%', height: '300px' }"></div>
        <transition  name="el-fade-in"><div v-if="showIV"><timeIV @isClosed="closeDeatil" :nodeIV="timeIV" /></div></transition>
    </div>

</template>
<style>
</style>
<script>
import timeIV from '@/components/timeIV'
import axios from 'axios'
export default {
  name: 'index',
  components: {
    timeIV,
  },
  data() {
    return{
      showIV: false,
      machineInfo:null,
      timeIV:null,
    }
  },
  methods:{
    closeDeatil(flag){
      this.showIV = false;
    },
    warnTransform(machineInfo){
      var timeList=machineInfo[0].timeNodeList;
      var warnArr=[];
      for(var i=0;i<timeList.length;i++){
        if(timeList[i].warning==0)  warnArr.push("正常");
        if(timeList[i].warning==1)  warnArr.push("开路");
        if(timeList[i].warning==2)  warnArr.push("短路");
        if(timeList[i].warning==3)  warnArr.push("阴影");
      }
      return warnArr;
    },
    timeTransform(machineInfo){
      var timeList=machineInfo[0].timeNodeList;
      var timeArr=[];
      for(var i=0;i<timeList.length;i++){
          timeArr.push(timeList[i].timeStamp);
      }
      return timeArr;
    },
    initEChart(){
      var _this=this;
      var timeArr=_this.timeTransform(this.machineInfo);
      var warnArr=_this.warnTransform(this.machineInfo);
      let myChart = this.$root.echarts.init(
        document.getElementById("myChart")
      );
      myChart.on('click',function(params){
        console.log(params.dataIndex);
        _this.timeIV=_this.machineInfo[0].timeNodeList[params.dataIndex];
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
          data: timeArr,
        },
        yAxis: {
          boundaryGap: false,
          
          data: [" ","正常","开路","短路","阴影",""],
        },
        series: [
          {
            name: "板1",
            type: "line",
            data: warnArr,
          },
        ],
        backgroundColor: 'rgba(255,255,255,0.8)',
      });
    },

    getData(){
      var getString='http://101.132.35.228:8080/api/getInfo';
      axios.get(getString).then(response => {
          var data = response.data;
          this.machineInfo=data;
          this.initEChart();
      });
    }

  },
  created(){
    this.getData();
  },
  mounted() {
    
  },
}
</script>


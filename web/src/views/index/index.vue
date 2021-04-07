<template>
    <div>
      <!-- <iframe width="400" scrolling="no" height="100" frameborder="0" allowtransparency="true" src="https://i.tianqi.com?c=code&id=35&icon=1&site=34"></iframe> -->
        <div  :style="{ width: '90%', height: '300px' }" 
          v-for="(mschine,index) in machineInfo" :key="index"
          :id="getID(index)" style="margin-bottom:30px;margin-left:80px;"></div>
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
    getID(index){
      return "chart"+index;
    },
    closeDeatil(flag){
      this.showIV = false;
    },
    warnTransform(machineInfo){
      var timeList=machineInfo.timeNodeList;
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
      var timeList=machineInfo.timeNodeList;
      var timeArr=[];
      for(var i=0;i<timeList.length;i++){
          timeArr.push(timeList[i].timeStamp);
      }
      return timeArr;
    },
    initEChart(index){
      this.$nextTick(function(){
      var _this=this;
      var timeArr=_this.timeTransform(this.machineInfo[index]);
      var warnArr=_this.warnTransform(this.machineInfo[index]);
      let myChart = this.$root.echarts.init(
        document.getElementById("chart"+index)
      );
      myChart.on('click',function(params){
        console.log(params.dataIndex);
        _this.timeIV=_this.machineInfo[index].timeNodeList[params.dataIndex];
        _this.showIV = true;
      });
      // 绘制图表
      myChart.setOption({
        title: { 
            text: _this.machineInfo[index].machineName,
            left:'center',
            top:20,
            subtext:'当前故障类型：'+warnArr.pop(),
        },
        tooltip: {},
        legend: {
          show:false,
        },
        xAxis: {
          boundaryGap: true,
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
             dataZoom: [
          {
               type: 'slider',
               show: true,
               xAxisIndex: [0],
               start: 1,
               minSpan:5,
               end: 100
          },
          {
               type: 'inside',
               xAxisIndex: [0],
               start: 1,
               minSpan:5,
               end: 100
          },
     ],
        backgroundColor: 'rgba(255,255,255,0.8)',
      });
    window.onresize = function () {//自适应大小
        myChart.resize();
      };
      });
    },

    getData(){
      var getString='http://101.132.35.228:8080/api/getInfo';
      axios.get(getString).then(response => {
          var data = response.data;
          data[1]=data[0];
          data[2]=data[0];
          data[3]=data[0];
          data[4]=data[0];
          data[5]=data[0];
          data[6]=data[0];
          data[7]=data[0];
          console.log(data);
          this.machineInfo=data;
          for(var i=0;i<data.length;i++){
              this.initEChart(i);
          }
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


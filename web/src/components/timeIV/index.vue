<template>
    <div class="detail">
            <div class="close" @click="closeDetail"><i class="el-icon-close" style="transform:scale(3);"></i></div>
            <div id="IVChart" :style="{ width: '100%', height: '80%' }" style="margin-top:50px;" ></div>
    </div>
</template>
<style>
.detail{
    background:#fff;
    width:70%;
    height:70%;
    position:fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    border-radius:10px;
     box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.close{
    float:right;
    
    margin:20px;
    transition:transform 0.2s ease-in-out;
}
.close:hover{
     transform:rotate(180deg);
}
</style>
<script>
import axios from 'axios'
export default{
    name: 'detailIV',
    props:['nodeIV'],
    data(){
      return{
          nodeID:this.nodeIV
      }
     },
    methods:{
        closeDetail(){
            this.$emit('isClosed', false);
        },
        initEChart(timeIV){
            console.log(timeIV);
            var _this=this;
                //this.$root => app
            let myChart = this.$root.echarts.init(
                document.getElementById("IVChart")
            );
            myChart.on('click',function(params){
                _this.showIV = true;
            });
            // 绘制图表
            myChart.setOption({
                title: { text: "   I-V曲线" },
                tooltip: {},
                legend: {
                    show:false,
                },
                xAxis: {
                    name:'电压',
                    boundaryGap: false,
                    scale:true,
                    data: timeIV.V,
                },
                yAxis: {
                name:'电流',
                type:"value",
                boundaryGap: false,
                scale:true,
                
                },
                series: [
                {
                    type: "line",
                    data: timeIV.I,
                    smooth: true,
                    
                },
                ],
                backgroundColor: 'rgba(255,255,255,0.8)',
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
            });
            window.onresize = function () {//自适应大小
                   myChart.resize();
                };
         },
    },
    mounted(){
        var getString='http://127.0.0.1:8080/api/getIV?id='+this.nodeID;
        axios.get(getString).then(response => {
            var data = response.data;
            console.log(response);
        });
        //this.initEChart(this.timeIV);
    }
}
</script>
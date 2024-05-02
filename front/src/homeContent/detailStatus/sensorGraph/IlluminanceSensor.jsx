import React from 'react';
import ReactApexChart from 'react-apexcharts';
import './illuminanceSensor.css';
import { useEffect } from 'react';

function IlluminanceSensor({ plantDetail, kitIlluminance }) {
    
    const [series, setSeries] = React.useState([kitIlluminance / 100]); 
    const plantLeastIlluminance = plantDetail?.plantLeastIlluminance || '';

    useEffect(() => {
        if (kitIlluminance === '') {
            setSeries(['notYet']);
        } else {
            setSeries([kitIlluminance / 100]);
        }
    }, [kitIlluminance]);


    function getIlluminanceStatus(plantLeastIlluminance, series) {
        const measuredIlluminance = series * 100;  

        if (measuredIlluminance >= plantLeastIlluminance && measuredIlluminance <= 10000) {
            return 'Optimal';
        }

        if (measuredIlluminance < plantLeastIlluminance) {
            return (
                <div style={{ textAlign: 'center' }}>
                    {`${plantLeastIlluminance - measuredIlluminance}Lux Below`}
                </div>
            )
        }

        if (measuredIlluminance > 10000) {
            return (
                <div style={{ textAlign: 'center' }}>
                    {`${measuredIlluminance - 10000}Lux Above`}
                </div>
            )
        }

        return 'No Data';
    }

    const options = {
        chart: {
            type: 'radialBar',
            offsetY: -20,
            parentHeightOffset: 0
        },
        plotOptions: {
            radialBar: {
                startAngle: -90,
                endAngle: 90,
                min: 0,
                max: 100,
                track: {
                    background: '#e7e7e7',
                    strokeWidth: '97%',
                    margin: 5,
                    dropShadow: {
                        enabled: true,
                        top: 2,
                        left: 0,
                        blur: 3,
                        opacity: 0.5
                    }
                },
                dataLabels: {
                    name: {
                        show: false
                    },
                    value: {
                        offsetY: 25,
                        fontSize: '18px',
                        formatter: function (val) { 
                            return val * 100 + 'Lux';
                        }
                    }
                },
            }
        },
        fill: {
            colors: ['#dfd880']
        },
        series: series,
        stroke: {
            lineCap: 'round'
        },
        labels: ['Illuminance'],
    }

    return (
        <div style={{ position: 'relative' }}>
            <ReactApexChart id='chart' options={options} series={series} type="radialBar" height='100%' paddingBottom="10px" />
            <div style={{
                position: 'absolute',
                left: '50%',
                transform: 'translateX(-50%)', 
                fontSize: '16px',
                top: '70%',
                bottom: '20%'
            }}>
                {getIlluminanceStatus(plantLeastIlluminance, series)}
            </div>

        </div>
    )
}

export default IlluminanceSensor;
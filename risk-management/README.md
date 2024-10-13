<!--
 * @Author: Joe
 * @Date: 2024-10-12 11:26:57
 * @LastEditors: joe skchan222@gmail.com
 * @LastEditTime: 2024-10-12 19:19:35
 * @FilePath: /racetrack-analytics/risk-management/README.md
 * @Description: 
 * 
 * Copyright (c) 2024, All Rights Reserved. 
-->
# Risk Management

Once we have the strategy of picking the positive expected horse, the most important thing is how to manage the risk.

Kelly Criterion was adopted as the risk management tools along with horse betting, I will hidden the complicated mathematics details here and try to express the basic concept of this tool.

Basically, Kelly equation try to minimize the possibility of bankrupt while maximize growth of wealth.

I have attached the example written in Java which can directly use for horse betting scenario. After you execute the KellyCalc.java, it will output as below.

```bash
$ java KellyCalc
the g(f) is 0.9097867003728842 should be equal to 0.9097867003728842
1=0.021637470835376753
2=0.0
3=0.0300688365544243
4=0.03214713762152495
5=0.0
6=0.006359854615789777
7=0.0
```

The Java class calculate the proportion of betting on each horse, for example, it should betting around 2.2% of wealth on horse number 1, approximately 220 if the wealth is $10,000

Thus, Kelly Criterion control the betting amount to prevent the deteriorated situation.

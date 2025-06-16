# Add a new form
On the file `forms.properties` add the form name and include the color limits. 

For example, for a form called `PersonalData`, configure it as follows:

```
forms.enabled=NCA, PersonalData


#NCA
form.nca.red.limit=-10
form.nca.orange.limit=-3
form.nca.yellow.limit=4
form.nca.light-green.limit=11

#PersonalData
form.personaldata.red.limit=0
form.personaldata.orange.limit=5
form.personaldata.yellow.limit=10
form.personaldata.light-green.limit=15
```

Where the limits for the colors are calculated in relation of the sum from all form variables with numerical values. 
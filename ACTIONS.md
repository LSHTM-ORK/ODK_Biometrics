# Actions

Keppel is designed to be used as an ["external app" for ODK Collect](https://docs.getodk.org/collect-external-apps/). This means it supports various "actions" that forms can use to trigger different features.

## Scan

The scan action (`uk.ac.lshtm.keppel.android.SCAN`) returns a hex encoded template (`text`) based on a scanned finger.

An optional `uk.ac.lshtm.keppel.android.return_iso_template.fast` parameter can be passed as `true` to have Keppel start scanning immediately rather than needing the enumerator or participant to press "Capture".

### Single field example

In `appearance`:

```
ex:uk.ac.lshtm.keppel.android.SCAN
```

### Multiple field example

In the group `body::intent`:

```
ex:uk.ac.lshtm.keppel.android.SCAN(uk.ac.lshtm.keppel.android.return_iso_template="my_iso_template_field", uk.ac.lshtm.keppel.android.return_nfiq="my_nfiq_field")
```

The returned template (`text`) or NFIQ (`integer`) can be included/omitted by including/omitting the corresponding parameter.

## Match

The match action (`uk.ac.lshtm.keppel.android.MATCH`) takes in a hex encoded template (`text`) and returns a score (`decimal`) based on matching against a capture.

An optional `uk.ac.lshtm.keppel.android.return_iso_template.fast` parameter can be passed as `true` to have Keppel start scanning immediately rather than needing the enumerator or participant to press "Capture".

### Single field example

In `appearance`:

```
ex:uk.ac.lshtm.keppel.android.MATCH(uk.ac.lshtm.keppel.android.iso_template=iso_template_field)
```

### Multiple field example

In the group `body::intent`:

```
ex:uk.ac.lshtm.keppel.android.MATCH(uk.ac.lshtm.keppel.android.iso_template=iso_template_field, 
uk.ac.lshtm.keppel.android.return_score="my_score_field", uk.ac.lshtm.keppel.android.return_iso_template="my_iso_template_field", uk.ac.lshtm.keppel.android.return_nfiq="my_nfiq_field")
```

The returned score (`decimal`), template (`text`) or NFIQ (`integer`) can be included/omitted by including/omitting the corresponding parameter.

## Multi-match

The multi-match (`uk.ac.lshtm.keppel.android.MULTI_MATCH`) action takes multiple hex encoded template parameters and returns the best (max) score matching against a capture.

An optional `uk.ac.lshtm.keppel.android.return_iso_template.fast` parameter can be passed as `true` to have Keppel start scanning immediately rather than needing the enumerator or participant to press "Capture".

### Single field example

In `appearance`:

```
ex:uk.ac.lshtm.keppel.android.MULTI_MATCH(uk.ac.lshtm.keppel.android.iso_template_1=iso_template_field_1, uk.ac.lshtm.keppel.android.iso_template_2=iso_template_field_2)
```

### Multiple field example

In the group `body::intent`:

```
ex:uk.ac.lshtm.keppel.android.MATCH(uk.ac.lshtm.keppel.android.iso_template_1=iso_template_field_1, uk.ac.lshtm.keppel.android.iso_template_2=iso_template_field_2, 
uk.ac.lshtm.keppel.android.return_score="my_score_field", uk.ac.lshtm.keppel.android.return_iso_template="my_iso_template_field", uk.ac.lshtm.keppel.android.return_nfiq="my_nfiq_field")
```

The returned score (`decimal`), template (`text`) or NFIQ (`integer`) can be included/omitted by including/omitting the corresponding parameter.
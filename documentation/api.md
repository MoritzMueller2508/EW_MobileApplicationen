#Description of the used API

- [Introduction](api.md#Introduction)
- [Specs](api.md#Specs)
- [Einbindung](api.md#Einbindung)

## Introduction

The Bing COVID-19 Data API provides total confirmed cases, deaths, and recovered by country. Responses are JSON formatted. This API does not implement authentication. Data is sourced from the Centers for Disease Control and Prevention, World Health Organization, and European Centre for Disease Prevention and Control. <br>
In our case, data is gathered in a created .csv file located in the filedrive of the phone - for offline functionality. <br>
After 24 hours, the .csv will be deleted and newly created to keep the data up-to-date.

---

## Specs

- API Endpoint: https://bing.com/covid/data
- API Portal / Home Page: https://bing.com/covid
- Primary Category: Coronavirus
- Secondary Category: COVID-19, Data, Location
- API Provider: Microsoft
- SSL Support: No
- Twitter URL: https://twitter.com/bing
- Authentication Model: Unspecified
- Version: 1.0
- Version status: Recommended (active, supported)
- Is the API Design/Description Non-Proprietary?: Yes
- Type: Web/Internet
- Scope: Single Purpose API
- Device Specific: No
- Architectural Style: REST
- Supported Request Formats: URI Query String/CRUD
- Supported Response Format: CSV
- Is This an Unofficial API?: No
- Is this a Hypermedia API?: No
- Restricted Access (Requires Provider Apporoval): No

(Data provided by https://www.programmableweb.com/api/bing-covid-19-data-rest-api-v10)

---


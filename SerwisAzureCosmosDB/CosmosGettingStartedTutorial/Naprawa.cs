using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace CosmosGettingStartedTutorial
{
    public class Naprawa
    {
        [JsonProperty(PropertyName = "id")]
        public int id;
        public String imie;
        public String nazwisko;
        public String data;
        public Samochod samo;
        public List<String> usterki;
    }
}

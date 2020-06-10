using System;
using System.Threading.Tasks;
using System.Configuration;
using System.Collections.Generic;
using System.Net;
using Microsoft.Azure.Cosmos;
using System.Collections;

namespace CosmosGettingStartedTutorial
{
    class Program
    {
        private static readonly string EndpointUri = ConfigurationManager.AppSettings["EndPointUri"];
        private static readonly string PrimaryKey = ConfigurationManager.AppSettings["PrimaryKey"];

        private CosmosClient cosmosClient;
        private Database database;
        private Container container;

        private string databaseId = "Serwis";
        private string containerId = "ToDoList";

        private bool koniec = false;

        public static async Task Main(string[] args)
        {
            try
            {
                Console.WriteLine("Beginning operations...\n");
                Program p = new Program();
                await p.GetStartedDemoAsync();
                

            }
            catch (CosmosException de)
            {
                Exception baseException = de.GetBaseException();
                Console.WriteLine("{0} error occurred: {1}", de.StatusCode, de);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error: {0}", e);
            }
            finally
            {
                Console.WriteLine("End of demo, press any key to exit.");
                Console.ReadKey();
            }
        }

        public async Task Menu()
        {
            while (!koniec)
            {
                await showMenu();
                await getWyborMenu();
            }
        }

        public async Task showMenu()
        {
            Console.WriteLine("\n**************  MENU:  ***************");
            Console.WriteLine("1 - Dodaj  ");
            Console.WriteLine("2 - Wyświetl  po  id ");
            Console.WriteLine("3 - Zaktualizuj ");
            Console.WriteLine("4 - Usuń po id");
            Console.WriteLine("0 - WYJŚCIE\n");
        }

        public async Task getWyborMenu()
        {
            int choice = -1;
            do
            {
                Console.WriteLine("Podaj wybór:");
                choice = Convert.ToInt32(Console.ReadLine());
                if (choice < 0 || choice > 4)
                {
                    Console.WriteLine("Brak takiej opcji!!!");
                }
            } while (choice < 0 || choice > 4);
            await akcja(choice);
        }

        public async Task akcja(int choice)
        {
            switch (choice)
            {
                case 1:
                    await addNew();
                    break;
                case 2:
                    await findById();
                    break;
                case 3:
                    //await update();
                    break;
                case 4:
                    //await delete();
                    break;
                case 0:
                    koniec = true;
                    break;
            }
        }

        private async Task addNew()
        {
            Naprawa naprawa = new Naprawa();
            Console.WriteLine("Podaj id:");
            naprawa.id = Convert.ToInt32(Console.ReadLine());
            Console.WriteLine("Podaj imie właściciela:");
            naprawa.imie = Console.ReadLine();
            Console.WriteLine("Podaj nazwisko właściciela:");
            naprawa.nazwisko = Console.ReadLine();
            Console.WriteLine("Podaj date serwisu:");
            naprawa.data = Console.ReadLine();
            Console.WriteLine("Podaj dane samochodu:");
            Samochod samochod = new Samochod();
            Console.WriteLine("Podaj marke samochodu:");
            samochod.marka = Console.ReadLine();
            Console.WriteLine("Podaj model samochodu:");
            samochod.model = Console.ReadLine();
            Console.WriteLine("Podaj rok produkcji samochodu:");
            samochod.rok = Convert.ToInt32(Console.ReadLine());
            Console.WriteLine("Podaj moc samochodu:");
            samochod.moc = Convert.ToInt32(Console.ReadLine());
            Console.WriteLine("Podaj numer rejestracyjny samochodu:");
            samochod.rej = Console.ReadLine();
            naprawa.samo = samochod;
            Console.WriteLine("Podaj liste usterek:");

            naprawa.usterki = new List<string>();
            bool koniecUst = false;
            while (!koniecUst)
            {
                String text = Console.ReadLine();
                if (text.Equals("0") == false)
                {
                    naprawa.usterki.Add(text);
                }
                else
                {
                    koniecUst = true;
                }
            }

            ItemResponse<Naprawa> response = await this.container.CreateItemAsync<Naprawa>(naprawa, new PartitionKey(naprawa.id));
        }

        private async Task findById()
        {
            Console.WriteLine("Podaj id:");
            string id = Console.ReadLine();

            var sqlQueryText = $"SELECT * FROM c WHERE c.Id = {id}";

            Console.WriteLine("Running query: {0}\n", sqlQueryText);

            QueryDefinition queryDefinition = new QueryDefinition(sqlQueryText);
            FeedIterator<Naprawa> queryResultSetIterator = this.container.GetItemQueryIterator<Naprawa>(queryDefinition);

            List<Naprawa> families = new List<Naprawa>();

            while (queryResultSetIterator.HasMoreResults)
            {
                FeedResponse<Naprawa> currentResultSet = await queryResultSetIterator.ReadNextAsync();
                foreach (Naprawa family in currentResultSet)
                {
                    families.Add(family);
                    Console.WriteLine("\tRead {0}\n", family);
                }
            }
        }


        public async Task GetStartedDemoAsync()
        {
            this.cosmosClient = new CosmosClient(EndpointUri, PrimaryKey, new CosmosClientOptions() { ApplicationName = "CosmosDBDotnetQuickstart" });
            await this.CreateDatabaseAsync();
            await this.CreateContainerAsync();
            await this.ScaleContainerAsync();
            await this.Menu();

            //await this.ScaleContainerAsync();
            /*await this.AddItemsToContainerAsync();
            await this.QueryItemsAsync();
            await this.ReplaceFamilyItemAsync();*/
        }
        private async Task CreateDatabaseAsync()
        {
            this.database = await this.cosmosClient.CreateDatabaseIfNotExistsAsync(databaseId);
            Console.WriteLine("Created Database: {0}\n", this.database.Id);
        }
        private async Task CreateContainerAsync()
        {
            this.container = await this.database.CreateContainerIfNotExistsAsync(containerId, "/LastName", 400);
            Console.WriteLine("Created Container: {0}\n", this.container.Id);
        }
        private async Task ScaleContainerAsync()
        {
            int? throughput = await this.container.ReadThroughputAsync();
            if (throughput.HasValue)
            {
                Console.WriteLine("Current provisioned throughput : {0}\n", throughput.Value);
                int newThroughput = throughput.Value + 100;
                // Update throughput
                await this.container.ReplaceThroughputAsync(newThroughput);
                Console.WriteLine("New provisioned throughput : {0}\n", newThroughput);
            }
            
        }
        private async Task AddItemsToContainerAsync()
        {
            // Create a family object for the Andersen family
            Family andersenFamily = new Family
            {
                Id = "Andersen.1",
                LastName = "Andersen",
                Parents = new Parent[]
                {
                    new Parent { FirstName = "Thomas" },
                    new Parent { FirstName = "Mary Kay" }
                },
                Children = new Child[]
                {
                    new Child
                    {
                        FirstName = "Henriette Thaulow",
                        Gender = "female",
                        Grade = 5,
                        Pets = new Pet[]
                        {
                            new Pet { GivenName = "Fluffy" }
                        }
                    }
                },
                Address = new Address { State = "WA", County = "King", City = "Seattle" },
                IsRegistered = false
            };

            try
            {
                // Read the item to see if it exists.  
                ItemResponse<Family> andersenFamilyResponse = await this.container.ReadItemAsync<Family>(andersenFamily.Id, new PartitionKey(andersenFamily.LastName));
                Console.WriteLine("Item in database with id: {0} already exists\n", andersenFamilyResponse.Resource.Id);
            }
            catch(CosmosException ex) when (ex.StatusCode == HttpStatusCode.NotFound)
            {
                // Create an item in the container representing the Andersen family. Note we provide the value of the partition key for this item, which is "Andersen"
                ItemResponse<Family> andersenFamilyResponse = await this.container.CreateItemAsync<Family>(andersenFamily, new PartitionKey(andersenFamily.LastName));

                // Note that after creating the item, we can access the body of the item with the Resource property off the ItemResponse. We can also access the RequestCharge property to see the amount of RUs consumed on this request.
                Console.WriteLine("Created item in database with id: {0} Operation consumed {1} RUs.\n", andersenFamilyResponse.Resource.Id, andersenFamilyResponse.RequestCharge);
            }

            // Create a family object for the Wakefield family
            Family wakefieldFamily = new Family
            {
                Id = "Wakefield.7",
                LastName = "Wakefield",
                Parents = new Parent[]
                {
                    new Parent { FamilyName = "Wakefield", FirstName = "Robin" },
                    new Parent { FamilyName = "Miller", FirstName = "Ben" }
                },
                Children = new Child[]
                {
                    new Child
                    {
                        FamilyName = "Merriam",
                        FirstName = "Jesse",
                        Gender = "female",
                        Grade = 8,
                        Pets = new Pet[]
                        {
                            new Pet { GivenName = "Goofy" },
                            new Pet { GivenName = "Shadow" }
                        }
                    },
                    new Child
                    {
                        FamilyName = "Miller",
                        FirstName = "Lisa",
                        Gender = "female",
                        Grade = 1
                    }
                },
                Address = new Address { State = "NY", County = "Manhattan", City = "NY" },
                IsRegistered = true
            };

            try
            {
                // Read the item to see if it exists
                ItemResponse<Family> wakefieldFamilyResponse = await this.container.ReadItemAsync<Family>(wakefieldFamily.Id, new PartitionKey(wakefieldFamily.LastName));
                Console.WriteLine("Item in database with id: {0} already exists\n", wakefieldFamilyResponse.Resource.Id);
            }
            catch(CosmosException ex) when (ex.StatusCode == HttpStatusCode.NotFound)
            {
                // Create an item in the container representing the Wakefield family. Note we provide the value of the partition key for this item, which is "Wakefield"
                ItemResponse<Family> wakefieldFamilyResponse = await this.container.CreateItemAsync<Family>(wakefieldFamily, new PartitionKey(wakefieldFamily.LastName));

                // Note that after creating the item, we can access the body of the item with the Resource property off the ItemResponse. We can also access the RequestCharge property to see the amount of RUs consumed on this request.
                Console.WriteLine("Created item in database with id: {0} Operation consumed {1} RUs.\n", wakefieldFamilyResponse.Resource.Id, wakefieldFamilyResponse.RequestCharge);
            }
        }
        private async Task QueryItemsAsync()
        {
            var sqlQueryText = "SELECT * FROM c WHERE c.LastName = 'Andersen'";

            Console.WriteLine("Running query: {0}\n", sqlQueryText);

            QueryDefinition queryDefinition = new QueryDefinition(sqlQueryText);
            FeedIterator<Family> queryResultSetIterator = this.container.GetItemQueryIterator<Family>(queryDefinition);

            List<Family> families = new List<Family>();

            while (queryResultSetIterator.HasMoreResults)
            {
                FeedResponse<Family> currentResultSet = await queryResultSetIterator.ReadNextAsync();
                foreach (Family family in currentResultSet)
                {
                    families.Add(family);
                    Console.WriteLine("\tRead {0}\n", family);
                }
            }
        }
        private async Task ReplaceFamilyItemAsync()
        {
            ItemResponse<Family> wakefieldFamilyResponse = await this.container.ReadItemAsync<Family>("Wakefield.7", new PartitionKey("Wakefield"));
            var itemBody = wakefieldFamilyResponse.Resource;
            
            // update registration status from false to true
            itemBody.IsRegistered = true;
            // update grade of child
            itemBody.Children[0].Grade = 6;

            // replace the item with the updated content
            wakefieldFamilyResponse = await this.container.ReplaceItemAsync<Family>(itemBody, itemBody.Id, new PartitionKey(itemBody.LastName));
            Console.WriteLine("Updated Family [{0},{1}].\n \tBody is now: {2}\n", itemBody.LastName, itemBody.Id, wakefieldFamilyResponse.Resource);
        }
        private async Task DeleteFamilyItemAsync()
        {
            var partitionKeyValue = "Wakefield";
            var familyId = "Wakefield.7";

            // Delete an item. Note we must provide the partition key value and id of the item to delete
            ItemResponse<Family> wakefieldFamilyResponse = await this.container.DeleteItemAsync<Family>(familyId,new PartitionKey(partitionKeyValue));
            Console.WriteLine("Deleted Family [{0},{1}]\n", partitionKeyValue, familyId);
        }
        private async Task DeleteDatabaseAndCleanupAsync()
        {
            DatabaseResponse databaseResourceResponse = await this.database.DeleteAsync();
            // Also valid: await this.cosmosClient.Databases["FamilyDatabase"].DeleteAsync();

            Console.WriteLine("Deleted Database: {0}\n", this.databaseId);

            //Dispose of CosmosClient
            this.cosmosClient.Dispose();
        }
 
    }
}

# RetrofitApi-part2

the objective of this workshop is to consume  API post using Retrofi2

The API used is the POST API of your Node express JS server that create a Product, we accede to this API using your IP adress
 http://192.168.1.18:8080/createProduct 

acceede to this api from Postman and extract models of this API 

# Step 1 
Create Jetpack Android Project
Add Retrofit and Gson dependency to your gradle
```kotlin
// Retrofit
implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation ("com.google.code.gson:gson:2.11.0")
```
> __retrofit2-kotlinx-serialization-converter__: A converter for Retrofit to use Kotlinx Serialization.

> __retrofit__: A type-safe HTTP client for making API requests.

> __gson__: A library for converting Java objects to/from JSON.
Add  serialization dependency
```kotlin
//Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
```
> __kotlinx-serialization-json__: A Kotlin library for serializing and deserializing JSON data using Kotlinx Serialization.
```kotlin
 //Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
```
# Step 2 
Add a package named model under your main package, in this create a model named Product.kt et ResponseProduct.kt using POJO Pluguin 
# Step 3
Create a package named network in your main package
in this package create an interface named ApiService containing the access to api
```kotlin
//Interface containing API
interface ApiService{
    @POST("createProduct")
    suspend fun  createProduct(@Body product:Product):ResponseProduct
}

```

in the same file add the BASE_URL and the retrofit instance
```kotlin
private var BASE_URL="http://192.168.1.18:8080/"
private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create()) // For JSON parsing
    .baseUrl(BASE_URL)
    .build()


```

add also the singleton that instantiate the singleton retrofit in the same package network
```kotlin
object ProductApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }}
```
> The ApiService interface defines HTTP requests for your API, including methods for GET, POST, Each method is annotated with HTTP verbs (like @GET, @POST), and the > response type is often wrapped in Suspend function (using Kotlin coroutines). The Retrofit instance converts these interface methods into real network calls.


# Step 4

create a package named viewmodel in witch create a file named ProductViewModel.kt

create the class ProductViewModel and the method createProduct and declare state variable prodUIState
```kotlin


class ProductViewModel: ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var prodUiState = mutableStateOf<ResponseProduct?>(null)


    fun createProduct(product: Product) {
        viewModelScope.launch {
            try {
                val result = ProductApi.retrofitService.createProduct(product)
                prodUiState.value=result
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
    }
}
```
> The ViewModel is responsible for requesting data from the API using retrofit. It holds the data for the UI, ensuring that it survives configuration changes (like screen rotations). The data retrieved from the API is stored in LiveData or StateFlow, allowing it to be observed by the UI.


# Step 5 

create package view, in witch create CreateProductScreen Composable that use viewmodel to add Product ,
```kotlin
@Composable
fun CreateProdScreen(productViewModel: ProductViewModel= viewModel(),modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var prix by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    val productresponse by remember{productViewModel.prodUiState}

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name= it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = prix,
            onValueChange = { prix= it },
            label = { Text("Prix") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = image,
            onValueChange = { image= it },
            label = { Text("Image") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val user = Product(name, description,prix.toInt(), listOf(image))
            productViewModel.createProduct(user)
        }) {
            Text("Create Product")
        }
        Spacer(modifier = Modifier.height(16.dp))

        productresponse?.let {
            Text("product Created: ${it.id}")
        }
    }
}
```
> the View (usually an Activity or Composable) is responsible for displaying the data and handling user interactions, while the ViewModel interacts with the data and API (like Retrofit). Here’s the specific role of the View when using Retrofit with a ViewModel is to get data. 
# Step 6
Prepare the Composable ProdApp that contain the main composable of the app
```kotlin
@Composable
fun ProdApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { PokTopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CreateProdScreen()

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier
    )
}
```

# Step 7
call your ProdApp in your main activity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitGet1Theme {
                    ProdApp()
                
            }
        }
    }
}
```


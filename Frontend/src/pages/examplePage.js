import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems', 'onGetAllItems', 'renderItems', 'onGetItemById', 'onGetItemsByCategory'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        document.getElementById('all-items').addEventListener('click', this.onGetAllItems);
        document.getElementById('search-id').addEventListener('click', this.onGetItemById);
        document.getElementById('search-id-category').addEventListener('click', this.onGetItemsByCategory);
        this.client = new ExampleClient();
        this.dataStore.addChangeListener(this.renderItems);
        this.renderItems();
    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async renderItems() {

        document.getElementById("allItemsToAdd").innerHTML = "";

        const mode = this.dataStore.get("eventHandler");

        let itemsHtml = "";

        if (mode === 1) {

            const items = this.dataStore.get("priorityItems");

            if (items) {
                for (const item of items) {
                    itemsHtml += `                         
                       <li class="cards_item">
                       <div class="card">
                            <div class="card_image"><img src="https://ih1.redbubble.net/image.1191483301.9610/flat,750x,075,f-pad,750x1000,f8f8f8.jpg" alt="insert image here"></div>
                            <div class="card_content">
                            <label class="label" for="item-title">Item ID: </label>
                             <h2 class="card_title" id="item-title">${item.itemId}</h2>
                            <br>
                            <label class="label" for="item-title">Description: </label>
                            <p class="card_text" id="description">${item.description}</p>
                            <label class="label" for="current">Current quantity: </label>                      
                            <p class="card_text" id="current">${item.currentQty}</p>
                            <label class="label" for="reorder">Reorder Quantity: </label>
                            <p class="card_text" id="reorder">${item.reorderQty}</p>
                            <label class="label" for="trigger">Quantity Trigger: </label>
                            <p class="card_text"id="trigger">${item.qtyTrigger}</p>
                            <label class="label" for="orderDate">Order Date: </label>
                            <p class="card_text" id="orderDate">${item.orderDate}</p>
                        </div>
                    </div>
                </li>              
                `;
                }
            }
        } else if (mode === 2) {

            const items = this.dataStore.get("items");

            if (items) {
                for (const item of items) {
                    itemsHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="https://ih1.redbubble.net/image.1191483301.9610/flat,750x,075,f-pad,750x1000,f8f8f8.jpg" alt="insert image here"></div>
                        <div class="card_content">
                            <label class="label" for="item-title">Item ID: </label>
                            <h2 class="card_title" id="item-title">${item.itemId}</h2>
                            <br>
                            <label class="label" for="item-title">Description: </label>
                            <p class="card_text" id="description">${item.description}</p>
                            <label class="label" for="current">Current quantity: </label>                      
                            <p class="card_text" id="current">${item.currentQty}</p>
                            <label class="label" for="reorder">Reorder Quantity: </label>
                            <p class="card_text" id="reorder">${item.reorderQty}</p>
                            <label class="label" for="trigger">Quantity Trigger: </label>
                            <p class="card_text" id="trigger">${item.qtyTrigger}</p>
                            <label class="label" for="orderDate">Order Date: </label>
                            <p class="card_text" id="orderDate">${item.orderDate}</p>                          
                        </div>
                    </div>
                </li>              
                `;
                }
            }
        }
        document.getElementById("allItemsToAdd").innerHTML += itemsHtml;
    }

    //Event handlers -------------------------------------------------------------------------------------------------

    async onGetPriorityItems() {
        closeItemForm();
        this.showMessage("Loading priority list....");
        const priorityItems = await this.client.getPriorityList(this.errorHandler);
        this.dataStore.set("eventHandler", 1);
        this.dataStore.set("priorityItems", priorityItems);
    }

    async onGetAllItems() {
        closeItemForm();
        const allItems = await this.client.getAllInventoryItems(this.errorHandler);
        this.dataStore.set("eventHandler", 2);
        this.dataStore.set("items", allItems);
    }

    async onGetItemById(event) {

        event.preventDefault();

        document.getElementById("form-card").innerHTML = "";

        openItemForm();
        closeSearchForm();

        const items = await this.client.getAllInventoryItems(this.errorHandler);

        const itemId = document.getElementById('item-id-id').value;

        let itemHtml = "";

        for (const item of items) {
            if (item && item.itemId === itemId) {
                itemHtml += `                                                                   
                    <div class="card">
                        <div class="card_image">
                        <img src="https://ih1.redbubble.net/image.1191483301.9610/flat,750x,075,f-pad,750x1000,f8f8f8.jpg" alt="insert image here">
                        </div>
                        <div class="card_content" style="background-color: white">
                            <label class="label" for="item-title">Item ID: </label>
                            <h2 class="card_title" id="item-title">${item.itemId}</h2>
                            <br>
                            <label class="label" for="item-title">Description: </label>
                            <p class="card_text" id="description">${item.description}</p>
                            <label class="label" for="current">Current quantity: </label>                      
                            <p class="card_text" id="current">${item.currentQty}</p>
                            <label class="label" for="reorder">Reorder Quantity: </label>
                            <p class="card_text" id="reorder">${item.reorderQty}</p>
                            <label class="label" for="trigger">Quantity Trigger: </label>
                            <p class="card_text"id="trigger">${item.qtyTrigger}</p>
                            <label class="label" for="orderDate">Order Date: </label>
                            <p class="card_text" id="orderDate">${item.orderDate}</p>
                             <button type="button" class="btn" onclick="closeItemForm()">Close</button>
                        </div>
                    </div>
                `;
            }
        }

        if (itemHtml === "") {
            itemHtml += `                                        
                    <div class="card">
                        <div class="card_image"><img src="https://www.vippng.com/png/detail/42-425681_kawaii-blue-sad-eyes-eye-blush-face-cute.png" alt="No Item Found"></div>
                        <div class="card_content">
                            <h2 class="card_title">Item id: "${itemId}" is invalid, Try searching again.</h2>                                               
                               <button type="button" class="btn" onclick="closeItemForm()">Close</button>
                        </div>
                    </div>             
                `;
        }
        document.getElementById("form-card").innerHTML = itemHtml;
    }

async onGetItemsByCategory(event) {

    event.preventDefault();

    closeItemForm();
    closeSearchForm();
    closeCategorySearchForm();

    const filter = document.getElementById('item-id-category').value;

    const categoryItems = await this.client.getItemsByCategory(filter, this.errorHandler);

    let itemsHtml = "";

    for (const item of categoryItems) {
        itemsHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iNzUycHQiIGhlaWdodD0iNzUycHQiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDc1MiA3NTIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiA8Zz4KICA8cGF0aCBkPSJtNDQ3LjA0IDQ1MS43OS0xNDAuMzUgNTguNDc3Yy0xLjAyMzQtMS45ODgzLTEuNzIyNy00LjEyMTEtMS43MjI3LTYuMzk4NHYtMzMuMTU2bDE0Mi4wNy01OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTMwNC45NiAzOTkuNjggMTQyLjA3LTU5LjE5OXY0MC4yNDZsLTE0Mi4wNyA1OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTQ0Ny4wNCA0ODIuNTZ2MjEuMzA1YzAgMy43NjU2LTEuNDk2MSA3LjM3ODktNC4xNTYyIDEwLjA0M2wtNTYuODM2IDU2LjgzMmMtMi43NzczIDIuNzc3My02LjQxMDIgNC4xNjQxLTEwLjA0NyA0LjE2NDFzLTcuMjY5NS0xLjM4NjctMTAuMDQzLTQuMTY0MWwtMzguMzk4LTM4LjM5OHoiLz4KICA8cGF0aCBkPSJtNTE4LjA3IDE5MS4zdjQyLjYyMWMwIDMuNzY1Ni0xLjQ5NjEgNy4zNzg5LTQuMTU2MiAxMC4wNDNsLTY0LjkyMiA2NC45MjYtMTQ0LjAzIDYwLjAxMnYtNTguMDU5bC02Ni44NzktNjYuODc1Yy0yLjY2NDEtMi42NjgtNC4xNjAyLTYuMjgxMi00LjE2MDItMTAuMDQ3di00Mi42MjFjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0NzcgMCAxNC4yMDcgNi4zNTk0IDE0LjIwNyAxNC4yMDd2NTYuODI4aDU2LjgyOGwwLjAwMzkwNy01Ni44MjhjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0MzggMCAxNC4yMDMgNi4zNTk0IDE0LjIwMyAxNC4yMDd6Ii8+CiA8L2c+Cjwvc3ZnPgo=" alt="insert logo here"></div>
                        <div class="card_content">
                            <label class="label" for="item-title">Item ID: </label>
                            <h2 class="card_title" id="item-title">${item.itemId}</h2>
                            <br>
                            <label class="label" for="item-title">Description: </label>
                            <p class="card_text" id="description">${item.description}</p>
                            <label class="label" for="current">Current quantity: </label>                      
                            <p class="card_text" id="current">${item.currentQty}</p>
                            <label class="label" for="reorder">Reorder Quantity: </label>
                            <p class="card_text" id="reorder">${item.reorderQty}</p>
                            <label class="label" for="trigger">Quantity Trigger: </label>
                            <p class="card_text"id="trigger">${item.qtyTrigger}</p>
                            <label class="label" for="orderDate">Order Date: </label>
                            <p class="card_text" id="orderDate">${item.orderDate}</p>
                        </div>
                    </div>
                </li>              
                `;
    }

    if (itemsHtml === "") {
        itemsHtml += `                 
                   <li class="cards_item">             
                    <div class="card">
                        <div class="card_image"><img src="https://www.vippng.com/png/detail/42-425681_kawaii-blue-sad-eyes-eye-blush-face-cute.png" alt="No Item Found"></div>
                        <div class="card_content">
                            <h2 class="card_title">No Items found with the filter: "${filter}", Try again.</h2>                                               
                        </div>
                    </div>
                    </li> 
                `;
    }
    document.getElementById("allItemsToAdd").innerHTML = itemsHtml;
}

// Event Handlers --------------------------------------------------------------------------------------------------

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const examplePage = new ExamplePage();
    examplePage.mount();
};

window.addEventListener('DOMContentLoaded', main);

import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems', 'onGetAllItems', 'renderItems', 'onGetItemById','onGetItemsByCategory'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        document.getElementById('all-items').addEventListener('click', this.onGetAllItems);
        document.getElementById('search-id').addEventListener('submit', this.onGetItemById);
        document.getElementById('search').addEventListener('click', this.onSearchItem);
        // document.getElementById('getItemsByCategory').addEventListener('click', this.onGetItemsByCategory);
        this.client = new ExampleClient();
        this.dataStore.addChangeListener(this.renderItems);
        this.renderItems();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderItems() {

        const mode = this.dataStore.get("eventHandler");

        let itemsHtml = "";

        if (mode === 1) {
            const items = this.dataStore.get("priorityItems");

            if (items) {
                for(const item of items) {
                    itemsHtml += `               
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="https://picsum.photos/500/300/?image=5" alt="item"></div>
                        <div class="card_content">
                            <h2 class="card_title">${item.itemId}</h2>
                            <p class="card_text">${item.description}</p>
                            <p class="card_text">${item.currentQty}</p>
                            <p class="card_text">${item.reorderQty}</p>
                            <p class="card_text">${item.qtyTrigger}</p>
                            <p class="card_text">${item.orderDate}</p>
                            <button class="btn card_btn">Update</button>
                        </div>
                    </div>
                </li>
                `;
                }
                this.showMessage(`Got priority items list!`);
            }
        } else if (mode === 2) {

            const items = this.dataStore.get("items");

            if (items) {
                for (const item of items) {
                    itemsHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="https://picsum.photos/500/300/?image=5" alt="item"></div>
                        <div class="card_content">
                            <h2 class="card_title">${item.itemId}</h2>
                            <p class="card_text">${item.description}</p>
                            <p class="card_text">${item.currentQty}</p>
                            <p class="card_text">${item.reorderQty}</p>
                            <p class="card_text">${item.qtyTrigger}</p>
                            <p class="card_text">${item.orderDate}</p>
                            <button class="btn card_btn">Update</button>
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
         const priorityItems = await this.client.getPriorityList(this.errorHandler);
         this.dataStore.set("eventHandler", 1);
         this.dataStore.set("priorityItems", priorityItems);
     }

    async onGetAllItems() {
        const allItems = await this.client.getAllInventoryItems(this.errorHandler);
        this.dataStore.set("eventHandler", 2);
        this.dataStore.set("items", allItems);
    }

    async onGetItemById(event) {

        event.preventDefault();

        const itemId = document.getElementById('item-id-id').value;

        let itemHtml = "";

        const item = await this.client.getItemById(itemId, this.errorHandler)

        if (!item.isEmpty()) {
            this.showMessage("Successful message found!")
            itemHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="https://picsum.photos/500/300/?image=5" alt="item"></div>
                        <div class="card_content">
                            <h2 class="card_title">${item.itemId}</h2>
                            <p class="card_text">${item.description}</p>
                            <p class="card_text">${item.currentQty}</p>
                            <p class="card_text">${item.reorderQty}</p>
                            <p class="card_text">${item.qtyTrigger}</p>
                            <p class="card_text">${item.orderDate}</p>
                            <button class="btn card_btn">Update</button>
                        </div>
                    </div>
                </li>              
                `;
        } else {
            itemHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="" alt="No Item Found"></div>
                        <div class="card_content">
                            <h2 class="card_title">Item id: ${itemId} is invalid, Try again.</h2>                         
                            <button class="btn card_btn">Update</button>
                        </div>
                    </div>
                </li>              
                `;
        }
        document.getElementById("search-id-form").innerHTML = itemHtml;
    }

    async onGetItemsByCategory() {

    }

    async fetchItem(itemId) {
        return await this.client.getItemById(itemId, this.errorHandler);
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

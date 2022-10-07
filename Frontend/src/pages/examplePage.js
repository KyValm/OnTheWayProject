import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems', 'onGetAllItems', 'renderItems'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        document.getElementById('all-items').addEventListener('click', this.onGetAllItems);
        this.client = new ExampleClient();
        this.dataStore.addChangeListener(this.renderItems);
        this.renderItems();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderItems() {

        const items = await this.client.getAllInventoryItems(this.errorHandler);
        const priorityItems = await this.client.getPriorityList(this.errorHandler);

        if(items && items.length > 0) {
            for (let item of items) {
                item.item = await this.fetchItem(item.itemId);
            }
        }

        if(priorityItems && priorityItems.length > 0) {
            for (let item of items) {
                item.item = await this.fetchItem(item.itemId);
            }
        }
        this.dataStore.set("items", items);
        this.dataStore.set("priorityItems", priorityItems);
    }

    async fetchItem(itemId) {
        return await this.client.getItemById(itemId, this.errorHandler);
    }

    //Event handlers -------------------------------------------------------------------------------------------------

     onGetPriorityItems() {

       const items = this.dataStore.get("priorityItems");

       let resultHTML = document.getElementById('allItemsToAdd');

        if (items) {
            for(const item of items) {
                resultHTML += `               
                    <div class="grid-item" id="${item.itemId}">
                        <td>${item.itemId}</td>
                        <td>${item.description}</td>
                        <td>${item.currentQty}</td>
                        <td>${item.reorderQty}</td>
                        <td>${item.qtyTrigger}</td>
                        <td>${item.orderDate}</td>
                        <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
                    </div>
                `;
            }
            this.showMessage(`Got priority items list!`);
        } else {
            this.errorHandler("Error generating priority list!  Try again...");
        }
         document.getElementById("allItemsToAdd").innerHTML += resultHTML;
    }

    async onGetAllItems() {

        let itemsHtml = "";

        const items = this.dataStore.get(this.errorHandler)

        if(items) {
            for (const item of items) {
                itemsHtml += `                                              
                    <tr class="grid-item" id="${item.itemId}">
                        <td>${item.itemId}</td>
                        <td>${item.description}</td>
                        <td>${item.currentQty}</td>                     
                        <td>${item.reorderQty}</td>
                        <td>${item.qtyTrigger}</td>
                        <td>${item.orderDate}</td>                                      
                        <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
                    </tr>                            
                `;
            }
        }
        document.getElementById("allItemsToAdd").innerHTML += itemsHtml;
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

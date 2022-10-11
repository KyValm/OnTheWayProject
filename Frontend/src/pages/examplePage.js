import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onUpdateItem','onGetPriorityItems', 'onGetAllItems', 'renderItems', 'onGetItemById','onGetItemsByCategory'], this);
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
        document.getElementById('search-update-id').addEventListener('click', this.onUpdateItem);
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

                for(const item of items) {
                    itemsHtml += `                         
                <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="https://static.thenounproject.com/png/1147665-200.png" alt="item"></div>
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
                        <div class="card_image"><img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iNzUycHQiIGhlaWdodD0iNzUycHQiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDc1MiA3NTIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiA8Zz4KICA8cGF0aCBkPSJtNDQ3LjA0IDQ1MS43OS0xNDAuMzUgNTguNDc3Yy0xLjAyMzQtMS45ODgzLTEuNzIyNy00LjEyMTEtMS43MjI3LTYuMzk4NHYtMzMuMTU2bDE0Mi4wNy01OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTMwNC45NiAzOTkuNjggMTQyLjA3LTU5LjE5OXY0MC4yNDZsLTE0Mi4wNyA1OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTQ0Ny4wNCA0ODIuNTZ2MjEuMzA1YzAgMy43NjU2LTEuNDk2MSA3LjM3ODktNC4xNTYyIDEwLjA0M2wtNTYuODM2IDU2LjgzMmMtMi43NzczIDIuNzc3My02LjQxMDIgNC4xNjQxLTEwLjA0NyA0LjE2NDFzLTcuMjY5NS0xLjM4NjctMTAuMDQzLTQuMTY0MWwtMzguMzk4LTM4LjM5OHoiLz4KICA8cGF0aCBkPSJtNTE4LjA3IDE5MS4zdjQyLjYyMWMwIDMuNzY1Ni0xLjQ5NjEgNy4zNzg5LTQuMTU2MiAxMC4wNDNsLTY0LjkyMiA2NC45MjYtMTQ0LjAzIDYwLjAxMnYtNTguMDU5bC02Ni44NzktNjYuODc1Yy0yLjY2NDEtMi42NjgtNC4xNjAyLTYuMjgxMi00LjE2MDItMTAuMDQ3di00Mi42MjFjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0NzcgMCAxNC4yMDcgNi4zNTk0IDE0LjIwNyAxNC4yMDd2NTYuODI4aDU2LjgyOGwwLjAwMzkwNy01Ni44MjhjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0MzggMCAxNC4yMDMgNi4zNTk0IDE0LjIwMyAxNC4yMDd6Ii8+CiA8L2c+Cjwvc3ZnPgo=" alt="item"></div>
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

    async onUpdateItem(event) {

        event.preventDefault();
        closeSearchForm();
        closeCategorySearchForm();

        const items = await this.client.getAllInventoryItems(this.errorHandler);

        const itemId = document.getElementById('item-update-id').value;

        let itemHtml = "";

        for (const item of items) {
            if (item && item.itemId === itemId) {
                itemHtml += `      
                   <li class="cards_item" id="${item.itemId}">                                                              
                    <div class="card">
                        <div class="card_image">
                        <img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iNzUycHQiIGhlaWdodD0iNzUycHQiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDc1MiA3NTIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiA8cGF0aCBkPSJtMzk3LjMxIDE5Ni45OWMtMjUuMTAyLTI0LjYyNS04MS40NTcgMTEuMzY3LTEyNi45MiA1NC40NjEtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0xLjQyMTkgMC45NDUzMS0wLjQ3MjY2IDAuNDcyNjYtMC40NzI2NiAwLjQ3MjY2LTAuNDcyNjYgMC45NDUzMWwtMC40NzI2NiAwLjQ3MjY2Yy0xLjg5NDUgMi44Mzk4LTEuODk0NSA3LjEwNTUgMC45NDUzMSA5LjQ3MjdsOS40NzI3IDkuNDcyNy02LjE1NjIgNi4xNTYyLTkuNDcyNy05LjQ3MjdjLTIuMzY3Mi0yLjM2NzItNi42Mjg5LTIuODM5OC05LjQ3MjctMC45NDUzMWwtMC40NzI2NiAwLjQ3MjY2Yy0wLjQ3MjY2IDAtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0wLjQ3MjY2IDAuNDcyNjYtMC40NzI2NiAwLjk0NTMxLTAuOTQ1MzEgMS40MjE5LTQyLjYyNSA0NC45OTItNzguNjE3IDEwMS4zNS01My45OTIgMTI2LjQ1IDAuNDcyNjYgMC40NzI2NiAwLjQ3MjY2IDAuNDcyNjYgMC45NDUzMSAwLjk0NTMxIDUuNjgzNiA1LjY4MzYgMTYuNTc0IDExLjM2NyAzNS45OTIgNy4xMDU1bDUzLjA0MyA3LjEwNTVjMS44OTQ1IDAuNDcyNjYgNC4yNjE3IDAuOTQ1MzEgNy4xMDU1IDIuMzY3MiAyLjgzOTggMS40MjE5IDYuNjI4OSAyLjgzOTggMTAuODkxIDMuNzg5MSAyMy4yMDcgNS4yMTA5IDYxLjU2Ni0zMC4zMDkgNzIuNDU3LTQxLjY3NiAxMC44OTEtMTEuMzY3IDQ2Ljg4My00OS4yNTQgNDEuNjc2LTcyLjQ1Ny0wLjk0NTMxLTQuMjYxNy0yLjgzOTgtOC4wNTA4LTMuNzg5MS0xMC44OTEtMS40MjE5LTIuODM5OC0yLjM2NzItNC43MzQ0LTIuMzY3Mi03LjEwNTVsLTguMDUwOC01Mi41N2M0LjI2MTctMTkuNDE4LTEuNDIxOS0zMC4zMDktNy4xMDU1LTM1Ljk5MiAwLTAuNDcyNjYtMC40NzI2Ni0wLjQ3MjY2LTAuNDcyNjYtMC45NDUzMXptLTExNS41NSA5My4yOTcgOC41MjM0LTguNTIzNCA0Ny44MzIgMzUuOTg4IDMuNzg5MSA4Ljk5NjFjMCAwLjQ3MjY2IDAuNDcyNjYgMC45NDUzMSAwLjk0NTMxIDEuNDIxOWwwLjQ3MjY2IDAuOTQ1MzF2MC45NDUzMWwtMTMuMjYyIDEzLjI2MmMtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0wLjk0NTMxIDBsLTAuOTQ1MzEtMC40Njg3NWMtMC40NzI2Ni0wLjQ3MjY2LTAuOTQ1MzEtMC40NzI2Ni0xLjQyMTktMC45NDUzMWwtOC45OTYxLTMuNzkzem0tNjcuMjQ2IDg5LjUwNGMtMS44OTQ1LTEuODk0NS0xLjg5NDUtMTAuODkxIDcuNTc4MS0yOC44ODcgOC45OTYxLTE3LjA1MSAyMy42OC0zNy40MTQgNDIuNjIxLTU3Ljc3N2wzNC41NyA0NS45MzhjLTQ0LjA0MyAzNy44ODctNzguMTQxIDQ2Ljg4My04NC43NyA0MC43Mjd6bTE3My4zMy04OS45OGMwLjk0NTMxIDUuNjgzNiAyLjgzOTggMTAuNDE4IDQuMjYxNyAxNC4yMDcgMC45NDUzMSAyLjM2NzIgMS44OTQ1IDQuMjYxNyAyLjM2NzIgNi4xNTYyIDAgNS4yMTA5LTkuOTQ1MyAyNC42MjUtMzQuNTcgNDkuNzI3LTI0LjYyNSAyNS4xMDItNDQuNTE2IDM1LjA0Ny00OS43MjcgMzQuNTctMS44OTQ1LTAuNDcyNjYtMy43ODkxLTEuNDIxOS02LjE1NjItMi4zNjcyLTMuNzg5MS0xLjg5NDUtOC4wNTA4LTMuNzg5MS0xNC4yMDctNC4yNjE3bC0xMi43ODUtMS44OTQ1YzE0LjY4LTguOTk2MSAyOC44ODctMjAuMzYzIDQyLjYyMS0zMi4yMDNsMC45NDUzMSAwLjQ3MjY2YzUuNjgzNiA0LjI2MTcgMTMuMjYyIDMuMzE2NCAxOC40NjktMS40MjE5bDEzLjI2Mi0xMy4yNjJjNC43MzQ0LTQuNzM0NCA1LjY4MzYtMTIuNzg1IDEuNDIxOS0xOC40NjlsLTAuNDcyNjYtMS40MjE5YzEyLjMxMi0xMy43MzQgMjMuNjgtMjguNDE0IDMyLjIwMy00Mi42MjF6bS00OS43MjcgOS40NzI3LTQ1LjkzOC0zNC41N2MyMC4zNjMtMTguOTQxIDQwLjcyNy0zNC4wOTggNTcuNzc3LTQyLjYyMSAxNy45OTYtOS40NzI3IDI3LjQ2OS04Ljk5NjEgMjguODg3LTcuNTc4MSA3LjEwNTUgNi42Mjg5LTEuODkwNiA0MC43MjctNDAuNzI3IDg0Ljc3em0xMDUuMTQgMzMuMTUyYzE3LjA1MSAxNy4wNTEtOS40NzI3IDUwLjE5OS0zNS4wNDcgNzUuNzczcy01OC43MjMgNTEuNjIxLTc1Ljc3MyAzNS4wNDdjLTQuNzM0NC00LjczNDQtNC43MzQ0LTEyLjc4NSAwLTE3LjUyMyAzLjc4OTEtMy43ODkxIDkuOTQ1My00LjczNDQgMTQuNjgtMi4zNjcyIDUuMjEwOS0xLjQyMTkgMjEuMzEyLTEwLjQxOCA0My41Ny0zMi42NzYgMjIuMjU4LTIyLjI1OCAzMS4yNTgtMzcuODg3IDMyLjY3Ni00My41Ny0yLjM2NzItNC43MzQ0LTEuODk0NS0xMC40MTggMi4zNjcyLTE0LjY4IDQuNzQyMi00Ljc0MjIgMTIuNzkzLTQuNzQyMiAxNy41MjctMC4wMDM5MDZ6bTMxLjczIDMxLjcyN2MxNy4wNTEgMTcuMDUxLTkuNDcyNyA1MC4xOTktMzUuMDQ3IDc1Ljc3My0yNS41NzQgMjUuNTc0LTU4LjcyMyA1MS42MjEtNzUuNzczIDM1LjA0Ny00LjczNDQtNC43MzQ0LTQuNzM0NC0xMi43ODUgMC0xNy41MjMgMy43ODkxLTMuNzg5MSA5Ljk0NTMtNC43MzQ0IDE0LjY4LTIuMzY3MiA1LjIxMDktMS40MjE5IDIxLjMxMi0xMC40MTggNDMuNTctMzIuNjc2IDIyLjI1OC0yMi4yNTggMzEuMjU4LTM3Ljg4NyAzMi42NzYtNDMuNTctMi4zNjcyLTQuNzM0NC0xLjg5NDUtMTAuNDE4IDIuMzY3Mi0xNC42OCA0LjczODMtNC43MzgzIDEyLjc4OS00LjczODMgMTcuNTI3LTAuMDAzOTA2em0zMS43MyAzMi4yMDdjMTcuMDUxIDE3LjA1MS05LjQ3MjcgNTAuMTk5LTM1LjA0NyA3NS43NzMtMjUuMTAyIDI1LjEwMi01OC43MjMgNTEuNjIxLTc1Ljc3MyAzNS4wNDctNC43MzQ0LTQuNzM0NC00LjczNDQtMTIuNzg1IDAtMTcuNTIzIDMuNzg5MS0zLjc4OTEgOS45NDUzLTQuNzM0NCAxNC42OC0yLjM2NzIgNS4yMTA5LTEuNDIxOSAyMS4zMTItMTAuNDE4IDQzLjU3LTMyLjY3NiAyMi4yNTgtMjIuMjU4IDMxLjI1OC0zNy44ODcgMzIuNjc2LTQzLjU3LTIuMzY3Mi00LjczNDQtMS44OTQ1LTEwLjQxOCAyLjM2NzItMTQuNjggNC43MzgzLTUuMjE0OCAxMi43ODktNS4yMTQ4IDE3LjUyNy0wLjAwMzkwNnptMTkuODkxIDQ5LjI1YzE2LjU3NCAxNi41NzQtMTYuNTc0IDUxLjE0OC0yMy4yMDcgNTcuNzc3LTYuNjI4OSA2LjYyODktNDEuMjAzIDM5Ljc4MS01Ny43NzcgMjMuMjA3LTQuNzM0NC00LjczNDQtNC43MzQ0LTEyLjc4NSAwLTE3LjUyMyAzLjMxNjQtMy4zMTY0IDguNTIzNC00LjI2MTcgMTIuNzg1LTIuODM5OCA0LjI2MTctMS40MjE5IDE0LjY4LTcuNTc4MSAyNy40NjktMjAuMzYzIDEyLjc4NS0xMi43ODUgMTguOTQxLTIzLjY4IDIwLjM2My0yNy40NjktMS40MjE5LTQuMjYxNy0wLjQ3MjY2LTguOTk2MSAyLjgzOTgtMTIuNzg1IDQuNzM4My01LjIxMDkgMTIuMzE2LTUuMjEwOSAxNy41MjctMC4wMDM5MDZ6bTIxLjMwOSA0NC45OTJjMC40NzI2NiAxLjQyMTkgMTUuMTU2IDI5LjgzNiAxNS4xNTYgNjEuNTY2IDAgMi44Mzk4LTAuOTQ1MzEgNS42ODM2LTMuMzE2NCA3LjU3ODEtMS44OTQ1IDEuODk0NS00LjczNDQgMy4zMTY0LTcuNTc4MSAzLjMxNjQtMzEuNzMgMC02MC42MTctMTQuNjgtNjEuNTY2LTE1LjE1Ni0zLjMxNjQtMS44OTQ1LTUuNjgzNi01LjIxMDktNS42ODM2LTguOTk2MSAwLTMuNzg5MSAxLjg5NDUtNy41NzgxIDUuMjEwOS05LjQ3MjcgMCAwIDguOTk2MS01LjY4MzYgMTguOTQxLTE1LjYyOSA0LjI2MTctNC4yNjE3IDEwLjg5MS00LjI2MTcgMTUuMTU2IDAgNC4yNjE3IDQuMjYxNyA0LjI2MTcgMTAuODkxIDAgMTUuMTU2LTIuMzY3MiAyLjM2NzItNC43MzQ0IDQuNzM0NC03LjEwNTUgNi42Mjg5IDYuNjI4OSAyLjM2NzIgMTUuMTU2IDQuMjYxNyAyMy42OCA1LjIxMDktMi44Mzk4LTIxLjc4NS0xMS44NC00MC4yNTQtMTEuODQtNDAuNzI3LTIuODM5OC01LjIxMDktMC40NzI2Ni0xMS44NCA0LjczNDQtMTQuNjggNS4yMTQ4LTIuMzc1IDExLjM3MS0wLjAwNzgxMiAxNC4yMTEgNS4yMDMxeiIvPgo8L3N2Zz4K" alt="item">
                        </div>
                        <div class="card_content">
                            <form>
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
                            <button class="btn card_btn" type="submit">Update</button>      
                            </form>                 
                        </div>
                    </div>
                  </li>           
                `;
            }
        }

        if (itemHtml === "") {
            itemHtml += `                 
                   <li class="cards_item">             
                    <div class="card">
                        <div class="card_image"><img src="https://as1.ftcdn.net/v2/jpg/01/51/80/22/1000_F_151802204_FzOADE0yF6o5o2UtqQ38F2dyyo9eeJbj.jpg" alt="No Item Found"></div>
                        <div class="card_content">
                            <h2 class="card_title">Item id: "${itemId}" is invalid, Try again.</h2>                                               
                        </div>
                    </div>
                    </li> 
                `;
        }
        document.getElementById("allItemsToAdd").innerHTML = itemHtml;
    }

     async onGetPriorityItems() {
         this.showMessage("Loading priority list....");
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

        closeSearchForm();

        const items = await this.client.getAllInventoryItems(this.errorHandler);

        const itemId = document.getElementById('item-id-id').value;

        let itemHtml = "";

        for (const item of items) {
            if (item && item.itemId === itemId) {
                itemHtml += `      
                   <li class="cards_item" id="${item.itemId}">                                                              
                    <div class="card">
                        <div class="card_image">
                        <img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iNzUycHQiIGhlaWdodD0iNzUycHQiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDc1MiA3NTIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiA8cGF0aCBkPSJtMzk3LjMxIDE5Ni45OWMtMjUuMTAyLTI0LjYyNS04MS40NTcgMTEuMzY3LTEyNi45MiA1NC40NjEtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0xLjQyMTkgMC45NDUzMS0wLjQ3MjY2IDAuNDcyNjYtMC40NzI2NiAwLjQ3MjY2LTAuNDcyNjYgMC45NDUzMWwtMC40NzI2NiAwLjQ3MjY2Yy0xLjg5NDUgMi44Mzk4LTEuODk0NSA3LjEwNTUgMC45NDUzMSA5LjQ3MjdsOS40NzI3IDkuNDcyNy02LjE1NjIgNi4xNTYyLTkuNDcyNy05LjQ3MjdjLTIuMzY3Mi0yLjM2NzItNi42Mjg5LTIuODM5OC05LjQ3MjctMC45NDUzMWwtMC40NzI2NiAwLjQ3MjY2Yy0wLjQ3MjY2IDAtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0wLjQ3MjY2IDAuNDcyNjYtMC40NzI2NiAwLjk0NTMxLTAuOTQ1MzEgMS40MjE5LTQyLjYyNSA0NC45OTItNzguNjE3IDEwMS4zNS01My45OTIgMTI2LjQ1IDAuNDcyNjYgMC40NzI2NiAwLjQ3MjY2IDAuNDcyNjYgMC45NDUzMSAwLjk0NTMxIDUuNjgzNiA1LjY4MzYgMTYuNTc0IDExLjM2NyAzNS45OTIgNy4xMDU1bDUzLjA0MyA3LjEwNTVjMS44OTQ1IDAuNDcyNjYgNC4yNjE3IDAuOTQ1MzEgNy4xMDU1IDIuMzY3MiAyLjgzOTggMS40MjE5IDYuNjI4OSAyLjgzOTggMTAuODkxIDMuNzg5MSAyMy4yMDcgNS4yMTA5IDYxLjU2Ni0zMC4zMDkgNzIuNDU3LTQxLjY3NiAxMC44OTEtMTEuMzY3IDQ2Ljg4My00OS4yNTQgNDEuNjc2LTcyLjQ1Ny0wLjk0NTMxLTQuMjYxNy0yLjgzOTgtOC4wNTA4LTMuNzg5MS0xMC44OTEtMS40MjE5LTIuODM5OC0yLjM2NzItNC43MzQ0LTIuMzY3Mi03LjEwNTVsLTguMDUwOC01Mi41N2M0LjI2MTctMTkuNDE4LTEuNDIxOS0zMC4zMDktNy4xMDU1LTM1Ljk5MiAwLTAuNDcyNjYtMC40NzI2Ni0wLjQ3MjY2LTAuNDcyNjYtMC45NDUzMXptLTExNS41NSA5My4yOTcgOC41MjM0LTguNTIzNCA0Ny44MzIgMzUuOTg4IDMuNzg5MSA4Ljk5NjFjMCAwLjQ3MjY2IDAuNDcyNjYgMC45NDUzMSAwLjk0NTMxIDEuNDIxOWwwLjQ3MjY2IDAuOTQ1MzF2MC45NDUzMWwtMTMuMjYyIDEzLjI2MmMtMC40NzI2NiAwLjQ3MjY2LTAuOTQ1MzEgMC40NzI2Ni0wLjk0NTMxIDBsLTAuOTQ1MzEtMC40Njg3NWMtMC40NzI2Ni0wLjQ3MjY2LTAuOTQ1MzEtMC40NzI2Ni0xLjQyMTktMC45NDUzMWwtOC45OTYxLTMuNzkzem0tNjcuMjQ2IDg5LjUwNGMtMS44OTQ1LTEuODk0NS0xLjg5NDUtMTAuODkxIDcuNTc4MS0yOC44ODcgOC45OTYxLTE3LjA1MSAyMy42OC0zNy40MTQgNDIuNjIxLTU3Ljc3N2wzNC41NyA0NS45MzhjLTQ0LjA0MyAzNy44ODctNzguMTQxIDQ2Ljg4My04NC43NyA0MC43Mjd6bTE3My4zMy04OS45OGMwLjk0NTMxIDUuNjgzNiAyLjgzOTggMTAuNDE4IDQuMjYxNyAxNC4yMDcgMC45NDUzMSAyLjM2NzIgMS44OTQ1IDQuMjYxNyAyLjM2NzIgNi4xNTYyIDAgNS4yMTA5LTkuOTQ1MyAyNC42MjUtMzQuNTcgNDkuNzI3LTI0LjYyNSAyNS4xMDItNDQuNTE2IDM1LjA0Ny00OS43MjcgMzQuNTctMS44OTQ1LTAuNDcyNjYtMy43ODkxLTEuNDIxOS02LjE1NjItMi4zNjcyLTMuNzg5MS0xLjg5NDUtOC4wNTA4LTMuNzg5MS0xNC4yMDctNC4yNjE3bC0xMi43ODUtMS44OTQ1YzE0LjY4LTguOTk2MSAyOC44ODctMjAuMzYzIDQyLjYyMS0zMi4yMDNsMC45NDUzMSAwLjQ3MjY2YzUuNjgzNiA0LjI2MTcgMTMuMjYyIDMuMzE2NCAxOC40NjktMS40MjE5bDEzLjI2Mi0xMy4yNjJjNC43MzQ0LTQuNzM0NCA1LjY4MzYtMTIuNzg1IDEuNDIxOS0xOC40NjlsLTAuNDcyNjYtMS40MjE5YzEyLjMxMi0xMy43MzQgMjMuNjgtMjguNDE0IDMyLjIwMy00Mi42MjF6bS00OS43MjcgOS40NzI3LTQ1LjkzOC0zNC41N2MyMC4zNjMtMTguOTQxIDQwLjcyNy0zNC4wOTggNTcuNzc3LTQyLjYyMSAxNy45OTYtOS40NzI3IDI3LjQ2OS04Ljk5NjEgMjguODg3LTcuNTc4MSA3LjEwNTUgNi42Mjg5LTEuODkwNiA0MC43MjctNDAuNzI3IDg0Ljc3em0xMDUuMTQgMzMuMTUyYzE3LjA1MSAxNy4wNTEtOS40NzI3IDUwLjE5OS0zNS4wNDcgNzUuNzczcy01OC43MjMgNTEuNjIxLTc1Ljc3MyAzNS4wNDdjLTQuNzM0NC00LjczNDQtNC43MzQ0LTEyLjc4NSAwLTE3LjUyMyAzLjc4OTEtMy43ODkxIDkuOTQ1My00LjczNDQgMTQuNjgtMi4zNjcyIDUuMjEwOS0xLjQyMTkgMjEuMzEyLTEwLjQxOCA0My41Ny0zMi42NzYgMjIuMjU4LTIyLjI1OCAzMS4yNTgtMzcuODg3IDMyLjY3Ni00My41Ny0yLjM2NzItNC43MzQ0LTEuODk0NS0xMC40MTggMi4zNjcyLTE0LjY4IDQuNzQyMi00Ljc0MjIgMTIuNzkzLTQuNzQyMiAxNy41MjctMC4wMDM5MDZ6bTMxLjczIDMxLjcyN2MxNy4wNTEgMTcuMDUxLTkuNDcyNyA1MC4xOTktMzUuMDQ3IDc1Ljc3My0yNS41NzQgMjUuNTc0LTU4LjcyMyA1MS42MjEtNzUuNzczIDM1LjA0Ny00LjczNDQtNC43MzQ0LTQuNzM0NC0xMi43ODUgMC0xNy41MjMgMy43ODkxLTMuNzg5MSA5Ljk0NTMtNC43MzQ0IDE0LjY4LTIuMzY3MiA1LjIxMDktMS40MjE5IDIxLjMxMi0xMC40MTggNDMuNTctMzIuNjc2IDIyLjI1OC0yMi4yNTggMzEuMjU4LTM3Ljg4NyAzMi42NzYtNDMuNTctMi4zNjcyLTQuNzM0NC0xLjg5NDUtMTAuNDE4IDIuMzY3Mi0xNC42OCA0LjczODMtNC43MzgzIDEyLjc4OS00LjczODMgMTcuNTI3LTAuMDAzOTA2em0zMS43MyAzMi4yMDdjMTcuMDUxIDE3LjA1MS05LjQ3MjcgNTAuMTk5LTM1LjA0NyA3NS43NzMtMjUuMTAyIDI1LjEwMi01OC43MjMgNTEuNjIxLTc1Ljc3MyAzNS4wNDctNC43MzQ0LTQuNzM0NC00LjczNDQtMTIuNzg1IDAtMTcuNTIzIDMuNzg5MS0zLjc4OTEgOS45NDUzLTQuNzM0NCAxNC42OC0yLjM2NzIgNS4yMTA5LTEuNDIxOSAyMS4zMTItMTAuNDE4IDQzLjU3LTMyLjY3NiAyMi4yNTgtMjIuMjU4IDMxLjI1OC0zNy44ODcgMzIuNjc2LTQzLjU3LTIuMzY3Mi00LjczNDQtMS44OTQ1LTEwLjQxOCAyLjM2NzItMTQuNjggNC43MzgzLTUuMjE0OCAxMi43ODktNS4yMTQ4IDE3LjUyNy0wLjAwMzkwNnptMTkuODkxIDQ5LjI1YzE2LjU3NCAxNi41NzQtMTYuNTc0IDUxLjE0OC0yMy4yMDcgNTcuNzc3LTYuNjI4OSA2LjYyODktNDEuMjAzIDM5Ljc4MS01Ny43NzcgMjMuMjA3LTQuNzM0NC00LjczNDQtNC43MzQ0LTEyLjc4NSAwLTE3LjUyMyAzLjMxNjQtMy4zMTY0IDguNTIzNC00LjI2MTcgMTIuNzg1LTIuODM5OCA0LjI2MTctMS40MjE5IDE0LjY4LTcuNTc4MSAyNy40NjktMjAuMzYzIDEyLjc4NS0xMi43ODUgMTguOTQxLTIzLjY4IDIwLjM2My0yNy40NjktMS40MjE5LTQuMjYxNy0wLjQ3MjY2LTguOTk2MSAyLjgzOTgtMTIuNzg1IDQuNzM4My01LjIxMDkgMTIuMzE2LTUuMjEwOSAxNy41MjctMC4wMDM5MDZ6bTIxLjMwOSA0NC45OTJjMC40NzI2NiAxLjQyMTkgMTUuMTU2IDI5LjgzNiAxNS4xNTYgNjEuNTY2IDAgMi44Mzk4LTAuOTQ1MzEgNS42ODM2LTMuMzE2NCA3LjU3ODEtMS44OTQ1IDEuODk0NS00LjczNDQgMy4zMTY0LTcuNTc4MSAzLjMxNjQtMzEuNzMgMC02MC42MTctMTQuNjgtNjEuNTY2LTE1LjE1Ni0zLjMxNjQtMS44OTQ1LTUuNjgzNi01LjIxMDktNS42ODM2LTguOTk2MSAwLTMuNzg5MSAxLjg5NDUtNy41NzgxIDUuMjEwOS05LjQ3MjcgMCAwIDguOTk2MS01LjY4MzYgMTguOTQxLTE1LjYyOSA0LjI2MTctNC4yNjE3IDEwLjg5MS00LjI2MTcgMTUuMTU2IDAgNC4yNjE3IDQuMjYxNyA0LjI2MTcgMTAuODkxIDAgMTUuMTU2LTIuMzY3MiAyLjM2NzItNC43MzQ0IDQuNzM0NC03LjEwNTUgNi42Mjg5IDYuNjI4OSAyLjM2NzIgMTUuMTU2IDQuMjYxNyAyMy42OCA1LjIxMDktMi44Mzk4LTIxLjc4NS0xMS44NC00MC4yNTQtMTEuODQtNDAuNzI3LTIuODM5OC01LjIxMDktMC40NzI2Ni0xMS44NCA0LjczNDQtMTQuNjggNS4yMTQ4LTIuMzc1IDExLjM3MS0wLjAwNzgxMiAxNC4yMTEgNS4yMDMxeiIvPgo8L3N2Zz4K" alt="item">
                        </div>
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

        if (itemHtml === "") {
            itemHtml += `                 
                   <li class="cards_item">             
                    <div class="card">
                        <div class="card_image"><img src="https://as1.ftcdn.net/v2/jpg/01/51/80/22/1000_F_151802204_FzOADE0yF6o5o2UtqQ38F2dyyo9eeJbj.jpg" alt="No Item Found"></div>
                        <div class="card_content">
                            <h2 class="card_title">Item id: "${itemId}" is invalid, Try searching again.</h2>                                               
                        </div>
                    </div>
                    </li> 
                `;
        }
        document.getElementById("allItemsToAdd").innerHTML = itemHtml;
    }

    async onGetItemsByCategory(event) {

        event.preventDefault();

        closeSearchForm();
        closeCategorySearchForm();

        const filter = document.getElementById("item-id-category").value;

        const items = await this.client.getItemsByCategory(filter, this.errorHandler);

        let itemsHtml = "";

        for (const item of items) {
                itemsHtml += `                                              
                    <li class="cards_item">
                    <div class="card">
                        <div class="card_image"><img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iNzUycHQiIGhlaWdodD0iNzUycHQiIHZlcnNpb249IjEuMSIgdmlld0JveD0iMCAwIDc1MiA3NTIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiA8Zz4KICA8cGF0aCBkPSJtNDQ3LjA0IDQ1MS43OS0xNDAuMzUgNTguNDc3Yy0xLjAyMzQtMS45ODgzLTEuNzIyNy00LjEyMTEtMS43MjI3LTYuMzk4NHYtMzMuMTU2bDE0Mi4wNy01OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTMwNC45NiAzOTkuNjggMTQyLjA3LTU5LjE5OXY0MC4yNDZsLTE0Mi4wNyA1OS4xOTl6Ii8+CiAgPHBhdGggZD0ibTQ0Ny4wNCA0ODIuNTZ2MjEuMzA1YzAgMy43NjU2LTEuNDk2MSA3LjM3ODktNC4xNTYyIDEwLjA0M2wtNTYuODM2IDU2LjgzMmMtMi43NzczIDIuNzc3My02LjQxMDIgNC4xNjQxLTEwLjA0NyA0LjE2NDFzLTcuMjY5NS0xLjM4NjctMTAuMDQzLTQuMTY0MWwtMzguMzk4LTM4LjM5OHoiLz4KICA8cGF0aCBkPSJtNTE4LjA3IDE5MS4zdjQyLjYyMWMwIDMuNzY1Ni0xLjQ5NjEgNy4zNzg5LTQuMTU2MiAxMC4wNDNsLTY0LjkyMiA2NC45MjYtMTQ0LjAzIDYwLjAxMnYtNTguMDU5bC02Ni44NzktNjYuODc1Yy0yLjY2NDEtMi42NjgtNC4xNjAyLTYuMjgxMi00LjE2MDItMTAuMDQ3di00Mi42MjFjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0NzcgMCAxNC4yMDcgNi4zNTk0IDE0LjIwNyAxNC4yMDd2NTYuODI4aDU2LjgyOGwwLjAwMzkwNy01Ni44MjhjMC03Ljg0NzcgNi4zNTk0LTE0LjIwNyAxNC4yMDctMTQuMjA3aDg1LjI0NmM3Ljg0MzggMCAxNC4yMDMgNi4zNTk0IDE0LjIwMyAxNC4yMDd6Ii8+CiA8L2c+Cjwvc3ZnPgo=" alt="item"></div>
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
                        <div class="card_image"><img src="https://as1.ftcdn.net/v2/jpg/01/51/80/22/1000_F_151802204_FzOADE0yF6o5o2UtqQ38F2dyyo9eeJbj.jpg" alt="No Item Found"></div>
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

class MyComponent extends HTMLElement {
  connectedCallback() {
    const template = document.createElement('template');
    template.innerHTML = `
      <style>
        .wrapper {
          background-color: #f9f9f9;
          padding: 10px;
        }

        .title {
          font-weight: bold;
        }
      </style>

      <div class="wrapper">
        <h2 class="title">My Component</h2>
        <slot></slot>
      </div>
    `;

    this.attachShadow({ mode: 'open' });
    this.shadowRoot.appendChild(template.content.cloneNode(true));
  }
}

customElements.define('my-component', MyComponent);
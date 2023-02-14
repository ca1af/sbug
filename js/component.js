class MyComponent extends HTMLElement {
  connectedCallback() {
    const template = document.createElement('template');
    template.innerHTML = `

        <h2 class="title">팝업 컴포넌트 테스트중</h2>
        <button onclick="">회원정보조회</button>
        <button onclick="location.href='updatemember.html'">회원정보수정</button>
        <button onclick="deleteMember()">회원탈퇴</button>
        <slot></slot>
      
    `;

    this.attachShadow({ mode: 'open' });
    this.shadowRoot.appendChild(template.content.cloneNode(true));
  }
}

customElements.define('my-component', MyComponent);
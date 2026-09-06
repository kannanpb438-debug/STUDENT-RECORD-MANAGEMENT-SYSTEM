/* ============================================================
   Student Record Management System (SRMS) - Main JavaScript
   Premium UI Interactions & Responsive Enhancements
   ============================================================ */

document.addEventListener('DOMContentLoaded', function () {

    // ---- Sidebar Toggle (Mobile/Tablet) ----
    initSidebarToggle();

    // ---- Tabbed Login Portal Selection ----
    initLoginTabs();

    // ---- Table Search Filter ----
    initTableSearch();

    // ---- Stat Counter Animation ----
    initCounterAnimation();

    // ---- Active Sidebar Link ----
    highlightActiveSidebarLink();

    // ---- Auto-dismiss Alerts ----
    initAlertAutoDismiss();
});

/* ============================================================
   Sidebar Toggle (Hamburger Menu)
   ============================================================ */
function initSidebarToggle() {
    const toggle = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (!toggle || !sidebar) return;

    toggle.addEventListener('click', function (e) {
        e.stopPropagation();
        sidebar.classList.toggle('open');
        if (overlay) {
            overlay.classList.toggle('active');
            overlay.style.display = sidebar.classList.contains('open') ? 'block' : 'none';
        }
    });

    if (overlay) {
        overlay.addEventListener('click', function () {
            closeSidebar(sidebar, overlay);
        });
    }

    // Close sidebar when clicking a link (mobile)
    const sidebarLinks = sidebar.querySelectorAll('.sidebar-link');
    sidebarLinks.forEach(function (link) {
        link.addEventListener('click', function () {
            if (window.innerWidth <= 768) {
                closeSidebar(sidebar, overlay);
            }
        });
    });

    // Close on Escape key
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && sidebar.classList.contains('open')) {
            closeSidebar(sidebar, overlay);
        }
    });
}

function closeSidebar(sidebar, overlay) {
    if (sidebar) sidebar.classList.remove('open');
    if (overlay) {
        overlay.classList.remove('active');
        setTimeout(function () { overlay.style.display = 'none'; }, 300);
    }
}

/* ============================================================
   Login Tab Selection
   ============================================================ */
function initLoginTabs() {
    const loginTabs = document.querySelectorAll('.login-tab');
    const portalRoleInput = document.getElementById('portalRoleInput');

    if (loginTabs.length > 0 && portalRoleInput) {
        loginTabs.forEach(function (tab) {
            tab.addEventListener('click', function () {
                loginTabs.forEach(function (t) { t.classList.remove('active'); });
                this.classList.add('active');
                portalRoleInput.value = this.getAttribute('data-role');
            });
        });
    }
}

/* ============================================================
   Table Search Filter (with debounce)
   ============================================================ */
function initTableSearch() {
    var debounceTimers = {};
    var searchInputs = document.querySelectorAll('.table-search-input');

    searchInputs.forEach(function (input) {
        input.addEventListener('keyup', function () {
            var inputEl = this;
            var tableId = inputEl.getAttribute('data-table');

            if (debounceTimers[tableId]) clearTimeout(debounceTimers[tableId]);

            debounceTimers[tableId] = setTimeout(function () {
                var filter = inputEl.value.toLowerCase();
                var table = document.getElementById(tableId);

                if (table) {
                    var rows = table.querySelectorAll('tbody tr');
                    var visibleCount = 0;

                    rows.forEach(function (row) {
                        if (row.classList.contains('no-results-row')) {
                            row.remove();
                            return;
                        }
                        var text = row.textContent.toLowerCase();
                        var match = text.indexOf(filter) > -1;
                        row.style.display = match ? '' : 'none';
                        if (match) visibleCount++;
                    });

                    // Show "no results" message
                    var tbody = table.querySelector('tbody');
                    var existingNoResults = tbody.querySelector('.no-results-row');
                    if (existingNoResults) existingNoResults.remove();

                    if (visibleCount === 0 && filter.length > 0) {
                        var colCount = table.querySelectorAll('thead th').length;
                        var noResultsRow = document.createElement('tr');
                        noResultsRow.className = 'no-results-row';
                        noResultsRow.innerHTML = '<td colspan="' + colCount + '" class="text-center text-muted" style="padding: 1.5rem;">No matching records found.</td>';
                        tbody.appendChild(noResultsRow);
                    }
                }
            }, 200);
        });
    });
}

/* ============================================================
   Stat Counter Animation
   ============================================================ */
function initCounterAnimation() {
    var statValues = document.querySelectorAll('.stat-value');

    statValues.forEach(function (el) {
        var text = el.textContent.trim();
        var num = parseFloat(text);

        if (!isNaN(num) && num > 0 && text.length < 10) {
            var suffix = text.replace(/[\d.,\s]/g, '');
            var isDecimal = text.indexOf('.') !== -1;
            var target = num;
            var duration = 800;
            var startTime = null;

            el.textContent = '0' + suffix;

            function animate(timestamp) {
                if (!startTime) startTime = timestamp;
                var progress = Math.min((timestamp - startTime) / duration, 1);
                var eased = 1 - Math.pow(1 - progress, 3); // ease-out cubic
                var current = target * eased;

                if (isDecimal) {
                    el.textContent = current.toFixed(1) + suffix;
                } else {
                    el.textContent = Math.floor(current) + suffix;
                }

                if (progress < 1) {
                    requestAnimationFrame(animate);
                } else {
                    el.textContent = text; // restore exact original
                }
            }

            // Use IntersectionObserver for scroll-triggered animation
            if ('IntersectionObserver' in window) {
                var observer = new IntersectionObserver(function (entries) {
                    entries.forEach(function (entry) {
                        if (entry.isIntersecting) {
                            requestAnimationFrame(animate);
                            observer.unobserve(el);
                        }
                    });
                }, { threshold: 0.3 });
                observer.observe(el);
            } else {
                requestAnimationFrame(animate);
            }
        }
    });
}

/* ============================================================
   Active Sidebar Link Highlighting
   ============================================================ */
function highlightActiveSidebarLink() {
    var currentPath = window.location.pathname;
    var sidebarLinks = document.querySelectorAll('.sidebar-link');

    sidebarLinks.forEach(function (link) {
        link.classList.remove('active');
        var href = link.getAttribute('href');
        if (href && currentPath.indexOf(href) !== -1 && href.length > 1) {
            link.classList.add('active');
        }
    });

    // Fallback: if none matched, activate first (Dashboard)
    var anyActive = document.querySelector('.sidebar-link.active');
    if (!anyActive && sidebarLinks.length > 0) {
        // Check if current path ends with /dashboard
        sidebarLinks.forEach(function (link) {
            var href = link.getAttribute('href');
            if (href && href.indexOf('dashboard') !== -1) {
                link.classList.add('active');
            }
        });
    }
}

/* ============================================================
   Alert Auto-Dismiss
   ============================================================ */
function initAlertAutoDismiss() {
    var alerts = document.querySelectorAll('.alert-success');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(function () { alert.remove(); }, 500);
        }, 5000);
    });
}

/* ============================================================
   Quick Fill Demo Credentials (Login Page)
   ============================================================ */
function fillDemoCredentials(username, password, role) {
    var userInput = document.getElementById('username');
    var passInput = document.getElementById('password');
    var roleInput = document.getElementById('portalRoleInput');
    var tabs = document.querySelectorAll('.login-tab');

    if (userInput && passInput) {
        userInput.value = username;
        passInput.value = password;

        // Visual feedback
        userInput.style.borderColor = '#10B981';
        passInput.style.borderColor = '#10B981';
        setTimeout(function () {
            userInput.style.borderColor = '';
            passInput.style.borderColor = '';
        }, 1200);
    }

    if (roleInput) {
        roleInput.value = role;
    }

    if (tabs) {
        tabs.forEach(function (tab) {
            if (tab.getAttribute('data-role') === role) {
                tab.classList.add('active');
            } else {
                tab.classList.remove('active');
            }
        });
    }
}

/* ============================================================
   Parent Dashboard Tab Switching
   ============================================================ */
function switchTab(element, sectionId) {
    document.querySelectorAll('.nav-tab-item').forEach(function (el) {
        el.classList.remove('active');
    });
    element.classList.add('active');
    scrollToSection(sectionId);
}

function scrollToSection(sectionId) {
    var sec = document.getElementById(sectionId);
    if (sec) {
        sec.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

/* ============================================================
   Registration Form Role Toggle
   ============================================================ */
function toggleRoleFields(role) {
    var studentDiv = document.getElementById('studentFields');
    var parentDiv = document.getElementById('parentFields');
    var facultyDiv = document.getElementById('facultyFields');
    var deptGroup = document.getElementById('deptGroup');

    if (studentDiv) studentDiv.style.display = 'none';
    if (parentDiv) parentDiv.style.display = 'none';
    if (facultyDiv) facultyDiv.style.display = 'none';
    if (deptGroup) deptGroup.style.display = 'block';

    if (role === 'FACULTY' && facultyDiv) {
        facultyDiv.style.display = 'block';
    } else if (role === 'PARENT' && parentDiv) {
        parentDiv.style.display = 'block';
        if (deptGroup) deptGroup.style.display = 'none';
    } else if (studentDiv) {
        studentDiv.style.display = 'block';
    }
}

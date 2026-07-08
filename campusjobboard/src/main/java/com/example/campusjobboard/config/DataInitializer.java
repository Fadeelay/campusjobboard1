package com.example.campusjobboard.config;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.enums.Status;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobRepository;
import com.example.campusjobboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           JobRepository jobRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (jobRepository.count() > 5) {
            log.info("Database already seeded — skipping DataInitializer");
            return;
        }

        log.info("Seeding database with sample employers and jobs...");

        User techCorp     = createEmployer("TechCorp Solutions",       "hr@techcorp.com");
        User designHub    = createEmployer("DesignHub Creative",        "hr@designhub.com");
        User dataVentures = createEmployer("DataVentures Analytics",    "hr@dataventures.com");
        User financeGroup = createEmployer("FinanceGroup International","hr@financegroup.com");
        User marketingPro = createEmployer("MarketingPro Agency",       "hr@marketingpro.com");
        User salesForce   = createEmployer("SalesForce Campus",         "hr@salesforce-campus.com");

        List<Job> jobs = List.of(

            // ── Engineering ────────────────────────────────────────────────────
            job(techCorp, "Software Developer Intern", "Engineering",
                "Remote, Toronto, Ontario", 22.0, LocalDate.of(2026, 9, 30),
                """
                We are looking for a motivated Software Developer Intern to join our engineering team.

                Responsibilities:
                - Develop and maintain web applications using Java and Spring Boot
                - Write clean, well-documented code following best practices
                - Collaborate with senior developers on feature development
                - Participate in code reviews and sprint planning

                Requirements:
                - Currently enrolled in a Computer Science or related degree program
                - Familiarity with Java, Python, or JavaScript
                - Understanding of REST APIs and databases
                - Strong problem-solving skills

                What we offer:
                - Fully remote position based in Toronto
                - Mentorship from senior engineers
                - $22/hr CAD competitive pay
                - Opportunity for full-time conversion after graduation"""),

            job(techCorp, "Cloud Platform Engineer", "Engineering",
                "Hybrid, Vancouver, British Columbia", 28.0, LocalDate.of(2026, 8, 15),
                """
                Join our Cloud Infrastructure team to help build and maintain scalable cloud solutions.

                Responsibilities:
                - Design and deploy cloud infrastructure on AWS/Azure
                - Automate infrastructure provisioning using Terraform or Ansible
                - Monitor system performance and respond to incidents
                - Collaborate with DevOps and development teams

                Requirements:
                - Pursuing a degree in Computer Science, IT, or related field
                - Knowledge of cloud platforms (AWS, Azure, or GCP)
                - Experience with Linux systems and shell scripting
                - Familiarity with containers (Docker/Kubernetes) is a plus

                Benefits:
                - Hybrid working (2 days in Vancouver office)
                - Competitive $28/hr CAD pay
                - Access to cloud certification vouchers"""),

            job(techCorp, "Full Stack Software Developer", "Engineering",
                "On-site, Waterloo, Ontario", 25.0, LocalDate.of(2026, 7, 31),
                """
                TechCorp is seeking a Full Stack Software Developer to work on our enterprise SaaS platform.

                Responsibilities:
                - Build responsive front-end interfaces using React or Angular
                - Develop RESTful APIs with Spring Boot or Node.js
                - Write unit and integration tests
                - Participate in agile sprints and daily standups

                Requirements:
                - Computer Science student (junior/senior year preferred)
                - Proficiency in JavaScript/TypeScript and Java or Python
                - Experience with SQL and NoSQL databases
                - Portfolio or GitHub with personal projects

                Perks:
                - On-site in Waterloo's tech corridor
                - Team lunches and social events
                - $25/hr CAD starting pay"""),

            job(techCorp, "DevOps Engineer Intern", "Engineering",
                "Hybrid, Ottawa, Ontario", 23.0, LocalDate.of(2026, 10, 1),
                """
                Help us build the next generation of our deployment and monitoring pipelines.

                Responsibilities:
                - Maintain CI/CD pipelines using GitHub Actions and Jenkins
                - Support containerized deployments with Docker and Kubernetes
                - Write infrastructure-as-code using Terraform
                - Assist in incident response and root cause analysis

                Requirements:
                - Pursuing degree in Computer Science, Engineering, or related field
                - Experience with at least one scripting language (Bash, Python)
                - Familiarity with version control (Git)
                - Eagerness to learn cloud-native technologies

                What we offer: $23/hr CAD, hybrid Ottawa, flexible schedule"""),

            job(techCorp, "Backend Software Developer", "Engineering",
                "Hybrid, Toronto, Ontario", 21.0, LocalDate.of(2026, 8, 31),
                """
                We are hiring a Backend Software Developer to strengthen our API team in Toronto.

                Responsibilities:
                - Design and build microservices using Java (Spring Boot)
                - Optimize database queries (PostgreSQL, MySQL)
                - Integrate third-party APIs and payment gateways
                - Write technical documentation

                Requirements:
                - Enrolled in a Software Engineering or CS degree program
                - Strong Java skills with OOP principles
                - Understanding of RESTful architecture
                - Ability to work independently and in a team

                Pay: $21/hr CAD | Hybrid (3 days in-office, Toronto)"""),

            job(techCorp, "Mobile App Developer Intern", "Engineering",
                "Hybrid, Calgary, Alberta", 20.0, LocalDate.of(2026, 9, 15),
                """
                Join our mobile team and help build the next generation of our consumer iOS and Android apps.

                Responsibilities:
                - Develop features for our React Native mobile application
                - Work closely with UI/UX designers to implement designs
                - Write automated tests and fix bugs
                - Participate in app store release processes

                Requirements:
                - Degree in Computer Science, Mobile Development, or equivalent
                - Experience with React Native, Flutter, or native iOS/Android
                - Familiarity with REST APIs and JSON
                - Published personal app or GitHub portfolio is a bonus

                Compensation: $20/hr CAD, hybrid Calgary office, flexible hours"""),

            // ── Design (UI/UX) ─────────────────────────────────────────────────
            job(designHub, "UI/UX Designer Intern", "Design",
                "Remote, Montreal, Quebec", 18.0, LocalDate.of(2026, 8, 1),
                """
                DesignHub is looking for a creative UI/UX Designer Intern to join our remote product team.

                Responsibilities:
                - Design wireframes, prototypes, and high-fidelity mockups in Figma
                - Conduct user research and usability testing
                - Collaborate with developers to ensure accurate implementation
                - Contribute to and maintain the design system

                Requirements:
                - Enrolled in a Design, HCI, or related degree program
                - Proficiency in Figma, Sketch, or Adobe XD
                - Portfolio showcasing UI/UX projects
                - Understanding of accessibility standards (WCAG)

                Compensation: $18/hr CAD, remote Montreal, flexible schedule"""),

            job(designHub, "Senior UX Designer (Student Co-op)", "Design",
                "Hybrid, Montreal, Quebec", 22.0, LocalDate.of(2026, 7, 15),
                """
                Join our award-winning design team for a 4-month co-op placement in Montreal.

                Responsibilities:
                - Lead end-to-end UX design for two major product features
                - Facilitate design sprints and workshops with stakeholders
                - Produce detailed interaction specs for engineering handoff
                - Present design decisions to senior leadership

                Requirements:
                - Graduate-level student in UX Design or HCI
                - 2+ years of design experience (internships count)
                - Expert Figma skills and strong portfolio
                - French language skills are an asset

                Pay: $22/hr CAD | Hybrid Montreal"""),

            job(designHub, "Product Designer", "Design",
                "On-site, Toronto, Ontario", 20.0, LocalDate.of(2026, 9, 1),
                """
                We need a Product Designer to shape the user experience of our B2B SaaS platform.

                Responsibilities:
                - Own the end-to-end design process from discovery to delivery
                - Create user journey maps, personas, and flow diagrams
                - Build and test interactive prototypes
                - Align designs with business goals and user needs

                Requirements:
                - Degree in Product Design, Interaction Design, or similar
                - Strong portfolio with real-world case studies
                - Experience with Figma and usability testing tools
                - Excellent communication and stakeholder management skills

                Benefits: $20/hr CAD, on-site Toronto downtown, team events"""),

            job(designHub, "Visual UI Designer", "Design",
                "Remote, Edmonton, Alberta", 17.0, LocalDate.of(2026, 10, 31),
                """
                Create beautiful, brand-consistent UI components for our growing product suite.

                Responsibilities:
                - Design high-quality visual assets, icons, and illustrations
                - Develop and maintain component libraries in Figma
                - Work with marketing to produce digital campaign assets
                - Ensure brand consistency across all touchpoints

                Requirements:
                - Pursuing degree in Graphic Design, Visual Communication, or UX Design
                - Strong visual design portfolio with web/mobile examples
                - Proficiency in Figma, Illustrator, and Photoshop
                - Eye for typography, colour, and layout

                Pay: $17/hr CAD | Fully remote, Edmonton-based"""),

            job(designHub, "UX Research Intern", "Design",
                "Hybrid, Vancouver, British Columbia", 19.0, LocalDate.of(2026, 8, 20),
                """
                Help us understand our users better through structured research programmes.

                Responsibilities:
                - Plan and conduct user interviews, surveys, and usability tests
                - Synthesize research findings into actionable insights
                - Create journey maps, affinity diagrams, and research reports
                - Present findings to product and design teams

                Requirements:
                - Enrolled in a Psychology, HCI, Design, or related degree
                - Interest in human behaviour and user-centred design
                - Strong analytical and written communication skills
                - Experience with tools like UserTesting or Optimal Workshop a plus

                Pay: $19/hr CAD | Hybrid Vancouver"""),

            // ── Research (Data Scientist) ──────────────────────────────────────
            job(dataVentures, "Data Scientist Intern", "Research",
                "Hybrid, Waterloo, Ontario", 20.0, LocalDate.of(2026, 9, 30),
                """
                DataVentures is seeking a Data Scientist Intern to work on real-world ML projects.

                Responsibilities:
                - Build and evaluate machine learning models (classification, regression)
                - Perform exploratory data analysis and feature engineering
                - Develop data pipelines using Python and SQL
                - Present findings and model performance to stakeholders

                Requirements:
                - Currently pursuing a degree in Data Science, Statistics, or CS
                - Proficiency in Python (pandas, scikit-learn, matplotlib)
                - Understanding of supervised and unsupervised learning
                - Strong mathematical foundation (linear algebra, statistics)

                Pay: $20/hr CAD | Hybrid Waterloo"""),

            job(dataVentures, "Machine Learning Engineer Intern", "Research",
                "Hybrid, Toronto, Ontario", 26.0, LocalDate.of(2026, 8, 15),
                """
                Join our AI/ML team to build production-grade machine learning systems.

                Responsibilities:
                - Develop, train, and deploy deep learning models
                - Optimize model performance and reduce inference latency
                - Integrate ML models into production APIs
                - Maintain MLflow experiment tracking and model registry

                Requirements:
                - CS, Statistics, or Engineering student (junior/senior year)
                - Proficiency in Python and PyTorch or TensorFlow
                - Experience deploying models with Flask, FastAPI, or similar
                - Familiarity with cloud ML services (AWS SageMaker, GCP Vertex AI)

                Pay: $26/hr CAD | Hybrid Toronto"""),

            job(dataVentures, "Research Data Analyst", "Research",
                "On-site, Ottawa, Ontario", 22.0, LocalDate.of(2026, 7, 31),
                """
                Help our research division extract meaning from large, complex datasets.

                Responsibilities:
                - Clean, transform, and analyse large datasets
                - Build dashboards and reports using Tableau or Power BI
                - Apply statistical methods to identify trends and anomalies
                - Collaborate with product and business teams on data requests

                Requirements:
                - Statistics, Mathematics, or Data Science student
                - Strong SQL skills and experience with Python or R
                - Experience creating visualisations with Tableau, Power BI, or Matplotlib
                - Attention to detail and strong analytical thinking

                Pay: $22/hr CAD | On-site Ottawa"""),

            job(dataVentures, "Applied Data Scientist", "Research",
                "Hybrid, Vancouver, British Columbia", 24.0, LocalDate.of(2026, 9, 15),
                """
                Apply data science techniques to solve meaningful business challenges.

                Responsibilities:
                - Develop predictive models for churn, forecasting, and segmentation
                - Work with engineers to productionise Python models
                - Design A/B tests and evaluate statistical significance
                - Present insights to non-technical stakeholders

                Requirements:
                - Graduate student in Data Science, Statistics, or related field
                - Expertise in Python ML stack (pandas, scikit-learn, XGBoost)
                - Experience with A/B testing and causal inference
                - Strong communication and presentation skills

                Pay: $24/hr CAD | Hybrid Vancouver"""),

            job(dataVentures, "Data Science Research Intern", "Research",
                "On-site, Montreal, Quebec", 18.0, LocalDate.of(2026, 10, 15),
                """
                Support our research division with data collection, analysis, and reporting.

                Responsibilities:
                - Collect and process structured and unstructured datasets
                - Build and validate predictive models
                - Assist in preparing research papers and internal reports
                - Perform literature reviews on ML methodologies

                Requirements:
                - First or second year student in Data Science, CS, or Statistics
                - Python programming skills (numpy, pandas)
                - Curiosity and passion for research
                - Strong written English and French skills

                Pay: $18/hr CAD | On-site Montreal"""),

            // ── Finance ────────────────────────────────────────────────────────
            job(financeGroup, "Finance Business Analyst Intern", "Finance",
                "Hybrid, Toronto, Ontario", 21.0, LocalDate.of(2026, 8, 31),
                """
                Join FinanceGroup's analytics team and gain hands-on experience in financial modelling.

                Responsibilities:
                - Support financial reporting and variance analysis
                - Build financial models in Excel and Python
                - Analyse P&L, balance sheet, and cash flow data
                - Prepare presentations for finance leadership

                Requirements:
                - Finance, Accounting, or Economics student
                - Strong Excel skills (pivot tables, VLOOKUP, financial functions)
                - Familiarity with financial statements
                - Detail-oriented with strong numerical reasoning

                Pay: $21/hr CAD | Hybrid Toronto"""),

            job(financeGroup, "Financial Data Analyst", "Finance",
                "Remote, Calgary, Alberta", 18.0, LocalDate.of(2026, 9, 1),
                """
                Analyse financial datasets to support investment and risk decisions.

                Responsibilities:
                - Aggregate and clean financial data from multiple sources
                - Build automated reports and dashboards in Power BI
                - Monitor KPIs and flag anomalies to the finance team
                - Support month-end close processes

                Requirements:
                - Finance, Accounting, or Business student
                - Proficiency in Excel and SQL
                - Experience with Power BI or Tableau preferred
                - Understanding of basic accounting principles

                Pay: $18/hr CAD | Fully remote, Calgary-based"""),

            job(financeGroup, "Investment Research Analyst Intern", "Finance",
                "On-site, Toronto, Ontario", 25.0, LocalDate.of(2026, 7, 15),
                """
                Work alongside our investment team to evaluate opportunities in Canadian markets.

                Responsibilities:
                - Screen equity opportunities using Bloomberg and FactSet
                - Build DCF and comparable company analysis models
                - Write investment memos and sector research reports
                - Attend earnings calls and summarise key takeaways

                Requirements:
                - Finance, Economics, or Accounting student (junior/senior)
                - Strong financial modelling skills (DCF, LBO basics)
                - CFA Level 1 progress is a strong plus
                - Passion for financial markets and investing

                Pay: $25/hr CAD | On-site Toronto Financial District"""),

            job(financeGroup, "Risk & Compliance Analyst", "Finance",
                "Hybrid, Ottawa, Ontario", 22.0, LocalDate.of(2026, 10, 31),
                """
                Support our Risk Management team in identifying and mitigating financial risk.

                Responsibilities:
                - Assist with credit risk assessments and due diligence
                - Review regulatory filings and compliance documentation
                - Monitor risk dashboards and escalate issues
                - Research regulatory developments (OSFI, Basel III)

                Requirements:
                - Finance, Law, or Risk Management student
                - Knowledge of financial risk frameworks
                - Strong attention to detail and research skills
                - Excellent written communication

                Pay: $22/hr CAD | Hybrid Ottawa (2 days in office)"""),

            // ── Operations ─────────────────────────────────────────────────────
            job(techCorp, "HR Business Analyst Intern", "Operations",
                "Remote, Halifax, Nova Scotia", 18.0, LocalDate.of(2026, 8, 30),
                """
                Support our People & Operations team with data-driven HR analysis.

                Responsibilities:
                - Analyse workforce data to identify trends in attrition and engagement
                - Support process mapping and improvement initiatives
                - Produce HR dashboards and weekly reports
                - Assist in the rollout of our new HRIS system

                Requirements:
                - HR, Business, or Information Systems student
                - Strong Excel and data analysis skills
                - Experience with HRIS platforms (Workday, SAP, or similar) is a plus
                - Strong interpersonal and communication skills

                Pay: $18/hr CAD | Fully remote, Halifax-based"""),

            job(techCorp, "Operations Business Analyst", "Operations",
                "Hybrid, Mississauga, Ontario", 20.0, LocalDate.of(2026, 9, 30),
                """
                Improve operational efficiency across our Ontario business units.

                Responsibilities:
                - Map and document current business processes (BPMN)
                - Identify inefficiencies and propose data-backed solutions
                - Support ERP implementation and testing
                - Liaise between IT and operations stakeholders

                Requirements:
                - Business Analysis, Management, or IT student
                - Analytical mindset with experience in process documentation
                - Familiarity with tools like Visio, Lucidchart, or JIRA
                - Strong communication and stakeholder management

                Pay: $20/hr CAD | Hybrid Mississauga"""),

            job(techCorp, "HR Coordinator Intern", "Operations",
                "On-site, Winnipeg, Manitoba", 16.0, LocalDate.of(2026, 10, 15),
                """
                Support day-to-day HR operations across our Manitoba office.

                Responsibilities:
                - Assist with recruitment coordination and interview scheduling
                - Maintain employee records and HR documentation
                - Support onboarding and offboarding processes
                - Help organise employee engagement initiatives

                Requirements:
                - HR, Business Administration, or Psychology student
                - Strong organisational and communication skills
                - Proficiency in MS Office Suite
                - Ability to handle confidential information with discretion

                Pay: $16/hr CAD | On-site Winnipeg"""),

            job(techCorp, "Supply Chain Operations Analyst", "Operations",
                "Remote, Edmonton, Alberta", 19.0, LocalDate.of(2026, 8, 1),
                """
                Help optimise our end-to-end supply chain with data and process analysis.

                Responsibilities:
                - Analyse inventory, procurement, and logistics data
                - Build dashboards to monitor supply chain KPIs
                - Identify bottlenecks and recommend improvements
                - Collaborate with vendors and internal teams

                Requirements:
                - Supply Chain, Business, or Industrial Engineering student
                - Proficiency in Excel and SQL
                - Interest in logistics and operations management
                - Strong problem-solving and communication skills

                Pay: $19/hr CAD | Fully remote, Edmonton-based"""),

            // ── Marketing ──────────────────────────────────────────────────────
            job(marketingPro, "Marketing Data Analyst Intern", "Marketing",
                "Hybrid, Toronto, Ontario", 17.0, LocalDate.of(2026, 9, 15),
                """
                Join our digital marketing team to drive data-driven campaign decisions.

                Responsibilities:
                - Analyse campaign performance across Google Ads, Facebook, and email
                - Build marketing dashboards in Looker or Tableau
                - Conduct competitor and market trend analysis
                - Support A/B testing of landing pages and email copy

                Requirements:
                - Marketing, Business, or Statistics student
                - Proficiency in Excel/Google Sheets and basic SQL
                - Familiarity with Google Analytics or similar tools
                - Creative thinker with strong analytical skills

                Pay: $17/hr CAD | Hybrid Toronto, 3 days in office"""),

            job(marketingPro, "Growth Marketing Intern", "Marketing",
                "Hybrid, Vancouver, British Columbia", 16.0, LocalDate.of(2026, 8, 1),
                """
                Accelerate user acquisition and retention through data-driven growth experiments.

                Responsibilities:
                - Run growth experiments across SEO, SEM, and social channels
                - Analyse funnel metrics and identify drop-off points
                - Write and test email marketing sequences
                - Assist in content strategy and SEO keyword research

                Requirements:
                - Marketing, Communications, or Business student
                - Experience with social media platforms and digital advertising
                - Familiarity with tools like HubSpot, Mailchimp, or Semrush
                - Data-driven mindset with strong copywriting skills

                Pay: $16/hr CAD | Hybrid Vancouver"""),

            job(marketingPro, "Digital Marketing Analyst", "Marketing",
                "Remote, Montreal, Quebec", 18.0, LocalDate.of(2026, 10, 31),
                """
                Own the analytics function for our digital marketing channels.

                Responsibilities:
                - Track and report on digital KPIs: CPC, CTR, ROAS, CAC, LTV
                - Build automated reports in Google Data Studio / Looker Studio
                - Perform cohort and attribution analysis
                - Present insights and recommendations to the marketing director

                Requirements:
                - Marketing Analytics, Statistics, or Business student
                - Proficiency in Google Analytics 4, Google Tag Manager
                - SQL skills for data extraction
                - Ability to translate data into clear business recommendations

                Pay: $18/hr CAD | Fully remote, Montreal-based"""),

            job(marketingPro, "Content & Social Media Intern", "Marketing",
                "Remote, Saskatoon, Saskatchewan", 15.0, LocalDate.of(2026, 9, 30),
                """
                Create engaging content that builds our brand across social and digital channels.

                Responsibilities:
                - Write blog posts, social captions, and email newsletters
                - Schedule and manage content calendars across LinkedIn, Instagram, and X
                - Monitor brand mentions and engage with our community
                - Support video script writing and production coordination

                Requirements:
                - Marketing, Communications, Journalism, or English student
                - Excellent writing and editing skills
                - Familiarity with social media platforms and scheduling tools
                - Creative and self-motivated with a strong eye for detail

                Pay: $15/hr CAD | Fully remote, Saskatoon-based"""),

            // ── Sales ──────────────────────────────────────────────────────────
            job(salesForce, "Sales Operations Analyst Intern", "Sales",
                "On-site, Toronto, Ontario", 19.0, LocalDate.of(2026, 8, 15),
                """
                Support our high-performing sales team with data, tools, and process optimisation.

                Responsibilities:
                - Maintain and optimise CRM data (Salesforce)
                - Generate weekly and monthly sales performance reports
                - Assist in territory planning and quota setting
                - Identify process inefficiencies and recommend improvements

                Requirements:
                - Business, Economics, or Data Analytics student
                - Familiarity with CRM tools (Salesforce, HubSpot) preferred
                - Strong Excel and data analysis skills
                - Detail-oriented with strong communication skills

                Pay: $19/hr CAD | On-site Toronto"""),

            job(salesForce, "Inside Sales Development Representative", "Sales",
                "Remote, Calgary, Alberta", 20.0, LocalDate.of(2026, 9, 1),
                """
                Kick-start your sales career as an SDR with one of the fastest-growing campus platforms.

                Responsibilities:
                - Prospect and qualify inbound and outbound leads
                - Conduct discovery calls with potential employer partners
                - Maintain accurate records in Salesforce CRM
                - Collaborate with account executives to close deals

                Requirements:
                - Business, Marketing, or Communications student
                - Strong verbal and written communication skills
                - Resilient, competitive, and target-driven
                - Prior sales or customer service experience is a plus

                Pay: $20/hr CAD base + uncapped commission | Remote, Calgary"""),

            job(salesForce, "B2B Sales Analyst", "Sales",
                "Hybrid, Ottawa, Ontario", 18.0, LocalDate.of(2026, 10, 1),
                """
                Analyse B2B sales pipelines and identify opportunities to increase conversion.

                Responsibilities:
                - Analyse sales pipeline data and identify trends
                - Build win/loss reports and competitive intelligence summaries
                - Support pricing analysis and proposal development
                - Coordinate between sales and marketing teams

                Requirements:
                - Business, Marketing, or Finance student
                - Strong analytical skills and proficiency in Excel
                - Interest in B2B software or services sales
                - Excellent presentation and communication skills

                Pay: $18/hr CAD | Hybrid Ottawa, 3 days in office"""),

            // ── Other ───────────────────────────────────────────────────────────
            job(dataVentures, "Research & Strategy Analyst", "Other",
                "On-site, Toronto, Ontario", 21.0, LocalDate.of(2026, 8, 31),
                """
                Support DataVentures' strategy division with market research and competitive analysis.

                Responsibilities:
                - Conduct primary and secondary market research
                - Analyse industry trends and compile intelligence reports
                - Support strategic planning presentations for senior leadership
                - Benchmark competitors across product, pricing, and GTM

                Requirements:
                - Business Strategy, Economics, or MBA student
                - Strong research, analytical, and writing skills
                - Proficiency in Excel and PowerPoint
                - Ability to synthesise complex information into clear narratives

                Pay: $21/hr CAD | On-site Toronto"""),

            job(techCorp, "IT Support & Systems Analyst Intern", "Other",
                "Remote, Victoria, British Columbia", 16.0, LocalDate.of(2026, 10, 31),
                """
                Provide technical support and assist with systems administration tasks.

                Responsibilities:
                - Respond to IT helpdesk tickets and resolve hardware/software issues
                - Assist in managing user accounts (Active Directory, Google Workspace)
                - Support network monitoring and maintenance tasks
                - Document IT processes and create user guides

                Requirements:
                - IT, Computer Science, or MIS student
                - Basic knowledge of networking (TCP/IP, DNS, DHCP)
                - Familiarity with Windows Server or Linux
                - Patient, customer-focused attitude

                Pay: $16/hr CAD | Fully remote, Victoria-based""")
        );

        jobRepository.saveAll(jobs);
        log.info("Seeded {} jobs successfully across all categories", jobs.size());
    }

    private User createEmployer(String fullName, String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setFullName(fullName);
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode("Password123!"));
            u.setRole(Role.EMPLOYER);
            u.setStatus(Status.ACTIVE);
            return userRepository.save(u);
        });
    }

    private Job job(User employer, String title, String category,
                    String location, double salary, LocalDate deadline, String description) {
        Job j = new Job();
        j.setEmployer(employer);
        j.setTitle(title);
        j.setCategory(category);
        j.setLocation(location);
        j.setSalary(salary);
        j.setDeadline(deadline);
        j.setDescription(description.stripIndent().strip());
        j.setStatus(JobStatus.APPROVED);
        return j;
    }
}
